package phoenix.templatesearcher.matchers;

import phoenix.templatesearcher.algo.FinalOptimizedLinkedPineForest;
import phoenix.templatesearcher.algo.FinalOptimizedLinkedPineNode;
import phoenix.templatesearcher.algo.FinalPineNode;
import phoenix.templatesearcher.algo.PineForest;
import phoenix.templatesearcher.algo.api.IOptimizedLinkedPineNode;
import phoenix.templatesearcher.api.ICharStream;
import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.api.IOccurrence;
import phoenix.templatesearcher.support.Occurrence;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class DynamicTemplateMatcher implements IMetaTemplateMatcher {
    /**
     * We need this constant to mark empty place for a forest.<br/>
     * Null values are badly handled by list iterator.
     */
    private static final FinalOptimizedLinkedPineForest EMPTY_FOREST = new FinalOptimizedLinkedPineForest();
    /**
     * Storage of pine forests.<br/>
     * Invariant: forests[i] contains pine forest that controls 2^i templates.<br/>
     * All forests contains different templates.
     */
    private List<FinalOptimizedLinkedPineForest> forests;
    /**
     * All templates are stored here - thus we check that each added template is unique.
     */
    private PineForest<FinalPineNode> allTemplates = new PineForest<>(new FinalPineNode());
    /**
     * Counter for template id.
     */
    private int idCounter = 0;

    public DynamicTemplateMatcher() {
        forests = new LinkedList<>();
    }

    /**
     * Adds forest with one line to the forests storage and merges them to keep {@link #forests} invariant.
     * @param forest forest to be added to the storage.
     */
    private void addOneLineForest(FinalOptimizedLinkedPineForest forest) {

        ListIterator<FinalOptimizedLinkedPineForest> iterator = forests.listIterator();

        // Inserting forest to appropriate free cell or merging forests.
        while (true) {
            if (!iterator.hasNext()) {
                forests.add(forest);
                break;
            } else {
                FinalOptimizedLinkedPineForest oldForest = iterator.next();

                if (oldForest == EMPTY_FOREST) {
                    iterator.set(forest);
                    break;
                } else {
                    forest = oldForest.cloneInto(forest);
                    iterator.set(EMPTY_FOREST);
                }
            }
        }
    }

    @Override
    public int addTemplate(String template) throws UnsupportedOperationException, IllegalArgumentException {
        if (template == null) {
            throw new NullPointerException("Template must not be null");
        }
        if (template.isEmpty()) {
            throw new IllegalArgumentException("Template must not be empty");
        }

        // Check if it is unique is inside.
        allTemplates.addLine(template.toCharArray(), idCounter);

        FinalOptimizedLinkedPineForest newForest = new FinalOptimizedLinkedPineForest();
        newForest.addLine(template.toCharArray(), idCounter);
        addOneLineForest(newForest);

        return idCounter++;
    }

    @Override
    public List<IOccurrence> matchStream(ICharStream stream) {

        List<IOccurrence> occurrences = new LinkedList<>();
        int index = 0;

        List<IOptimizedLinkedPineNode<FinalOptimizedLinkedPineNode>> nodes = new LinkedList<>();

        forests.forEach(
                (forest) -> {
                    if (forest != null) {
                        nodes.add(forest);
                    }
                });

        while (!stream.isEmpty()) {
            char nextChar = stream.nextChar();
            ListIterator<IOptimizedLinkedPineNode<FinalOptimizedLinkedPineNode>> nodesIterator =
                    nodes.listIterator();

            // Go by one char in each pine forest.
            while (nodesIterator.hasNext()) {
                IOptimizedLinkedPineNode<FinalOptimizedLinkedPineNode> node =
                        nodesIterator.next().go(nextChar);

                List<? extends IOptimizedLinkedPineNode<FinalOptimizedLinkedPineNode>> matches =
                        node.listEndsOfLine();

                for (IOptimizedLinkedPineNode<FinalOptimizedLinkedPineNode> match : matches) {
                    occurrences.add(new Occurrence(index, match.getEndOfLineID()));
                }

                nodesIterator.set(node);
            }

            index++;
        }

        return occurrences;
    }

}
