package phoenix.templatesearcher.algo;

import phoenix.templatesearcher.algo.api.IPineForest;
import phoenix.templatesearcher.algo.api.IPineNode;
import phoenix.templatesearcher.algo.api.IPineNodeVisitor;
import phoenix.templatesearcher.exception.DuplicateLineException;
import phoenix.templatesearcher.support.IdentifiedLine;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * A complete implementation of pine forest.<br/>
 * {@link phoenix.templatesearcher.algo.api.ICharComparator} variety is not supported, but this forest
 * compares characters like {@link phoenix.templatesearcher.algo.api.ICharComparator#DEFAULT_COMPARATOR}.
 * @param <Node>
 *         node type on which the forest is built.
 */
public class PineForest<Node extends IPineNode<Node>> implements IPineNode<Node>, IPineForest {
    protected final Node root;

    public PineForest(Node root) {
        if (!root.isRoot()) {
            throw new IllegalArgumentException("Given node is not a root");
        }
        this.root = root;
    }

    public PineForest(Supplier<Node> rootSupplier) {
        this(rootSupplier.get());
    }

    @Override
    public int getID() {
        return root.getID();
    }

    @Override
    public boolean isEndOfLine() {
        return root.isEndOfLine();
    }

    @Override
    public void setEndOfLine(int lineID) {
        root.setEndOfLine(lineID);
    }

    @Override
    public int getEndOfLineID() {
        return root.getEndOfLineID();
    }

    @Override
    public Node go(char symbol) {
        return root.go(symbol);
    }

    @Override
    public Node add(char symbol) throws IllegalArgumentException {
        return root.add(symbol);
    }

    @Override
    public Node makeBoundNode(Node parent, char parentChar) {
        return root.makeBoundNode(parent, parentChar);
    }

    @Override
    public Node makeNode() {
        return root.makeNode();
    }

    @Override
    public Node thisNode() {
        return root.thisNode();
    }

    @Override
    public Node getParent() {
        return root.getParent();
    }

    @Override
    public void walkSubtree(IPineNodeVisitor<IPineNode<Node>> visitor) {
        root.walkSubtree(visitor);
    }

    @Override
    public boolean isRoot() {
        return root.isRoot();
    }

    @Override
    public String getLine() {
        return root.getLine();
    }

    @Override
    public char getParentChar() {
        return root.getParentChar();
    }

    @Override
    public void addLine(char[] line, int lineID) throws IllegalArgumentException, DuplicateLineException {
        addLine(
                () -> new Iterator<Character>() {
                    int index = 0;

                    @Override
                    public boolean hasNext() {
                        return index < line.length;
                    }

                    @Override
                    public Character next() {
                        if (!hasNext()) {
                            throw new NoSuchElementException("No more characters");
                        }
                        return line[index++];
                    }
                }, lineID);
    }

    @Override
    public void addLine(Iterable<Character> data, int lineID)
            throws IllegalArgumentException, DuplicateLineException {
        if (lineID == IPineNode.NOT_END_OF_LINE_ID) {
            throw new IllegalArgumentException("Unsupported line ID given");
        }

        IPineNode node = root;

        for (Character c : data) {
            node = node.add(c);
        }
        if (node.isEndOfLine()) {
            StringBuilder sb = new StringBuilder();
            data.forEach((symbol) -> sb.append(symbol));
            throw new DuplicateLineException(
                    "Line has been already added: " + sb.toString(), node.getEndOfLineID());
        }
        node.setEndOfLine(lineID);
    }

    @Override
    public String[] listLines() throws IllegalStateException {
        List<IdentifiedLine> identifiedLines = listIdentifiedLines();
        String[] array = new String[identifiedLines.size()];

        for (IdentifiedLine line : identifiedLines) {
            if (line.getID() < 0 || line.getID() > array.length) {
                throw new IllegalStateException("Improper ids found");
            }
            if (array[line.getID()] != null) {
                throw new IllegalStateException(
                        "At least two different templates have equal id. Implementation error.");
            }
            array[line.getID()] = line.getLine();
        }

        return array;
    }

    @Override
    public List<IdentifiedLine> listIdentifiedLines() {
        LinesExtractor extractor = new LinesExtractor();
        root.walkSubtree(extractor);
        return extractor.getLines();
    }

    /**
     * Adds lines from this forest to the target forest with preserved identity numbers.
     * @param target
     *         pine forest to add lines to.
     * @return target pine forest.
     * @throws java.lang.IllegalArgumentException
     * @throws phoenix.templatesearcher.exception.DuplicateLineException
     * @see #addLine(char[], int)
     */
    public <F extends PineForest<Node>> F cloneInto(F target)
            throws IllegalArgumentException, DuplicateLineException {
        for (IdentifiedLine myLine : listIdentifiedLines()) {
            target.addLine(myLine.getLine().toCharArray(), myLine.getID());
        }
        return target;
    }

    /**
     * Class for walking pine forest and pulling lines from it.
     */
    private class LinesExtractor implements IPineNodeVisitor<IPineNode<Node>> {
        private final List<IdentifiedLine> lines = new LinkedList<>();

        public List<IdentifiedLine> getLines() {
            return lines;
        }

        @Override
        public void visit(IPineNode<Node> node) {
            if (node.isEndOfLine()) {
                lines.add(new IdentifiedLine(node.getLine(), node.getEndOfLineID()));
            }
        }
    }
}
