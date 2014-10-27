package phoenix.templatesearcher.matchers;

import phoenix.templatesearcher.algo.IOptimizedLinkedPineNode;
import phoenix.templatesearcher.algo.OptimizedLinkedPineForest;
import phoenix.templatesearcher.api.ICharStream;
import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.api.IOccurrence;
import phoenix.templatesearcher.support.Occurrence;

import java.util.LinkedList;
import java.util.List;

public class StaticTemplateMatcher implements IMetaTemplateMatcher {
    /**
     * Used for templates store and matches search.
     */
    private OptimizedLinkedPineForest templatesForest = new OptimizedLinkedPineForest();

    /**
     * Counter for template identities.
     */
    private int idCounter = 0;

    /**
     * Indicates whether adding templates is forbidden.
     */
    private boolean forbidAddTemplate = false;

    @Override
    public int addTemplate(String template) throws UnsupportedOperationException {
        if (forbidAddTemplate) {
            throw new UnsupportedOperationException(
                    "Cannot add templates after a first call to matchStream(ICharStream) is done");
        }
        if (template.isEmpty()) {
            throw new IllegalArgumentException("Template must not be empty");
        }

        templatesForest.addLine(template.toCharArray(), idCounter);
        return idCounter++;
    }

    @Override
    public List<IOccurrence> matchStream(ICharStream stream) {
        forbidAddTemplate = true;

        List<IOccurrence> occurrences = new LinkedList<>();

        IOptimizedLinkedPineNode node = templatesForest;

        int index = 0;

        while (!stream.isEmpty()) {
            char nextChar = stream.nextChar();
            node = (IOptimizedLinkedPineNode) node.go(nextChar);
            List<? extends IOptimizedLinkedPineNode> matches = node.listEndsOfLine();

            for (IOptimizedLinkedPineNode match : matches) {
                occurrences.add(new Occurrence(index, match.getEndOfLineID()));
            }

            index++;
        }

        return occurrences;
    }

}
