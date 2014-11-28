package phoenix.templatesearcher.matchers;

import phoenix.templatesearcher.algo.FinalOptimizedLinkedPineForest;
import phoenix.templatesearcher.algo.FinalOptimizedLinkedPineNode;
import phoenix.templatesearcher.algo.api.IOptimizedLinkedPineNode;
import phoenix.templatesearcher.api.ICharStream;
import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.api.IOccurrence;
import phoenix.templatesearcher.support.Occurrence;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class StaticTemplateMatcher implements IMetaTemplateMatcher {
    /**
     * Used for templates store and matches search.
     */
    private FinalOptimizedLinkedPineForest templatesForest;

    /**
     * Counter for template identities.
     */
    private int idCounter = 0;

    /**
     * Indicates whether adding templates is forbidden.
     */
    private boolean forbidAddTemplate;

    public StaticTemplateMatcher() {
        templatesForest = new FinalOptimizedLinkedPineForest();
        forbidAddTemplate = false;
    }

    StaticTemplateMatcher(FinalOptimizedLinkedPineForest forest) {
        templatesForest = forest;
        forbidAddTemplate = true;
    }

    @Override
    public int addTemplate(String template) throws UnsupportedOperationException {
        if (forbidAddTemplate) {
            throw new UnsupportedOperationException(
                    "Cannot add templates after a first call to matchStream(ICharStream) is done");
        }

        Objects.requireNonNull(template, "Template must not be null");
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

        IOptimizedLinkedPineNode<FinalOptimizedLinkedPineNode> node = templatesForest;

        int index = 0;

        while (!stream.isEmpty()) {
            char nextChar = stream.nextChar();
            node = node.go(nextChar);
            List<? extends IOptimizedLinkedPineNode> matches = node.listEndsOfLine();

            for (IOptimizedLinkedPineNode match : matches) {
                occurrences.add(new Occurrence(index, match.getEndOfLineID()));
            }

            index++;
        }

        return occurrences;
    }

}
