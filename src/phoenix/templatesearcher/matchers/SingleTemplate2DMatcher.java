package phoenix.templatesearcher.matchers;

import phoenix.templatesearcher.algo.FinalOptimizedLinkedPineForest;
import phoenix.templatesearcher.algo.FinalOptimizedLinkedPineNode;
import phoenix.templatesearcher.algo.PrefixFunction;
import phoenix.templatesearcher.algo.api.IOptimizedLinkedPineNode;
import phoenix.templatesearcher.api.IMatrix2D;
import phoenix.templatesearcher.api.IMetaTemplate2DMatcher;
import phoenix.templatesearcher.api.IOccurrence2D;
import phoenix.templatesearcher.exception.DuplicateLineException;
import phoenix.templatesearcher.support.Occurrence2D;
import phoenix.templatesearcher.support.Utility;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * Matcher that can find occurrences of one 2-dimensional character matrix in another.
 */
public class SingleTemplate2DMatcher implements IMetaTemplate2DMatcher {
    // We have to search occurrences of matrix n*m in matrix N*M for O(N*M + n*m) time & memory.

    // At first, suppose we can compare strings for O(1).
    // Let string with length = m be an atomic object. We can compare atomic objects for O(1).
    // In string with length = M we can choose M - m + 1 of such atomic objects.
    // We can find occurrences of n atomic objects in column of N atomic objects for O(N + n) using prefix
    // function algorithm. So, we can find occurrences of one matrix in another for O(N*M + n*M) = O(N*M).

    // Now let us understand, how we can compare two strings for O(1) not using hash algorithms.
    // Create a pine forest on n strings with length m (rows of the template matrix).
    // If a line [j] is equal to line [i] (i < j), then let's store this link somehow.
    // For each line with length M (rows of the big matrix) let's find occurrences of lines (one max,
    // because all lines are fixed length) in the pine forest for O(M).
    // For the whole matrix it will be O(M * N).

    // In total, we have O(|A| + |B|) time and memory.

    private static final int TEMPLATE_ID = 0;

    /**
     * Template matrix. We are going to search its occurrences in another matrix.
     */
    private IMatrix2D template;

    /**
     * Equality class of template row. Shows to which row each row is equal.<br/>
     * If there are duplicate rows, only one of them is equal to itself, other are equal to him.
     */
    private Integer[] templateRowClass;

    private static final Integer ROW_CLASS_EQUAL_TO_NOTHING = -1;

    private static final BiPredicate<Integer, Integer> ROW_CLASS_COMPARATOR = (a, b) -> a.equals(b);

    /**
     * Precounted prefix function for template row classes.
     */
    private int[] templatePrefixFunction;

    /**
     * Pine forest that stores rows of the template matrix.
     */
    private FinalOptimizedLinkedPineForest templateRowsForest;

    @Override
    public int addTemplate(IMatrix2D template)
            throws UnsupportedOperationException, IllegalArgumentException {
        Objects.requireNonNull(template, "Template must not be null");
        if (template.getWidth() <= 0 || template.getHeight() <= 0) {
            throw new IllegalArgumentException("Template matrix must have positive width and height");
        }

        if (this.template != null) {
            throw new UnsupportedOperationException("Cannot add more then one template");
        }

        this.template = template;

        templateRowClass = new Integer[template.getHeight()];

        templateRowsForest = new FinalOptimizedLinkedPineForest();

        for (int i = 0; i < templateRowClass.length; i++) {
            try {
                templateRowsForest.addLine(Utility.iterableThroughRow(template, i), i);
                templateRowClass[i] = i;
            } catch (DuplicateLineException exc) {
                templateRowClass[i] = exc.getAssignedID();
            }
        }

        templatePrefixFunction = new int[templateRowClass.length];

        PrefixFunction.countPrefixFunction(
                templateRowClass, templatePrefixFunction, 0, templateRowClass.length, ROW_CLASS_COMPARATOR);

        return TEMPLATE_ID;
    }

    @Override
    public List<IOccurrence2D> matchMatrix(IMatrix2D matrix) {
        Objects.requireNonNull(matrix, "Searchable matrix must not be null");

        List<IOccurrence2D> occurrences = new LinkedList<>();

        if (template.getHeight() > matrix.getHeight() || template.getWidth() > matrix.getWidth()) {
            return occurrences;
        }

        // Count of large columns. Large column has width = template.getWidth();
        int largeColumnsCount = matrix.getWidth() - template.getWidth() + 1;

        // objectClass[col - template.getWidth()][row] = class.
        Integer[][] objectClass = new Integer[largeColumnsCount][matrix.getHeight()];

        // Counter for classes that are unique.
        int uniqueClassCounter = templateRowClass.length;

        // Need to determine for each object in matrix: which template object it is equal to.
        for (int row = 0; row < matrix.getHeight(); row++) {
            // Our object is a string with length = template.getWidth().
            Iterator<Character> rowIterator = Utility.iterableThroughRow(matrix, row).iterator();

            IOptimizedLinkedPineNode<FinalOptimizedLinkedPineNode> node = templateRowsForest;

            for (int i = 1; i < template.getWidth(); i++) {
                node = node.go(rowIterator.next());
            }

            for (int column = 0; column < largeColumnsCount; column++) {
                node = node.go(rowIterator.next());
                List<FinalOptimizedLinkedPineNode> endsOfLines = node.listEndsOfLine();
                if (endsOfLines.isEmpty()) {
                    objectClass[column][row] = uniqueClassCounter++;
                } else if (endsOfLines.size() == 1) {
                    objectClass[column][row] = endsOfLines.get(0).getEndOfLineID();
                } else {
                    throw new IllegalStateException(
                            "A line cannot be equal to more than one line of the same length");
                }
            }
        }

        // Now we can build prefix function for each column and find occurrences of full template matrix.
        for (int column = 0; column < largeColumnsCount; column++) {
            int[] pi = new int[templatePrefixFunction.length + 1 + matrix.getHeight()];
            System.arraycopy(templatePrefixFunction, 0, pi, 0, templatePrefixFunction.length);

            Integer[] dataClass = new Integer[pi.length];
            int dataOffset = templateRowClass.length + 1;

            System.arraycopy(templateRowClass, 0, dataClass, 0, templateRowClass.length);
            dataClass[templateRowClass.length] = ROW_CLASS_EQUAL_TO_NOTHING;
            System.arraycopy(objectClass[column], 0, dataClass, dataOffset, objectClass[column].length);

            PrefixFunction
                    .countPrefixFunction(dataClass, pi, dataOffset, dataClass.length, ROW_CLASS_COMPARATOR);

            for (int i = dataOffset; i < dataClass.length; i++) {
                if (pi[i] == templateRowClass.length) {
                    int x = column + template.getWidth() - 1;
                    int y = i - dataOffset;

                    occurrences.add(new Occurrence2D(x, y, TEMPLATE_ID));
                }
            }
        }

        return occurrences;
    }
}
