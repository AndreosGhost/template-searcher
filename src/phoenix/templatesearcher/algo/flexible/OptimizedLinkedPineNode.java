package phoenix.templatesearcher.algo.flexible;

import phoenix.templatesearcher.algo.api.IOptimizedLinkedPineNode;

import java.util.LinkedList;
import java.util.List;

/**
 * Simple implementation of {@link phoenix.templatesearcher.algo.api.IOptimizedLinkedPineNode}.
 */
public abstract class OptimizedLinkedPineNode<Node extends OptimizedLinkedPineNode<Node>>
        extends LinkedPineNode<Node> implements IOptimizedLinkedPineNode<Node> {
    private Node nextMarkedSuffix;

    public OptimizedLinkedPineNode() {
        super();
    }

    public OptimizedLinkedPineNode(Node parent, char parentChar) {
        super(parent, parentChar);
    }

    @Override
    public Node obtainNextMarkedSuffix() {
        if (isRoot()) {
            return null;
        }
        if (nextMarkedSuffix == null) {
            Node link = obtainLink();
            if (link.isEndOfLine()) {
                nextMarkedSuffix = link;
            } else {
                nextMarkedSuffix = link.obtainNextMarkedSuffix();
            }
        }
        return nextMarkedSuffix;
    }

    @Override
    public List<Node> listEndsOfLine() {
        List<Node> endsOfLines = new LinkedList<>();
        if (isEndOfLine()) {
            endsOfLines.add(thisNode());
        }
        Node nextEndOfLine = obtainNextMarkedSuffix();
        while (nextEndOfLine != null) {
            endsOfLines.add(nextEndOfLine);
            nextEndOfLine = nextEndOfLine.obtainNextMarkedSuffix();
        }
        return endsOfLines;
    }
}
