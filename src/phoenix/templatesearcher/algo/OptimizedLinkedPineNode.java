package phoenix.templatesearcher.algo;

import java.util.LinkedList;
import java.util.List;

/**
 * Simple implementation of {@link phoenix.templatesearcher.algo.IOptimizedLinkedPineNode}.
 */
public class OptimizedLinkedPineNode extends LinkedPineNode implements IOptimizedLinkedPineNode {
    private OptimizedLinkedPineNode nextMarkedSuffix;

    public OptimizedLinkedPineNode() {
        super();
    }

    public OptimizedLinkedPineNode(IPineNode parent, char parentChar) {
        super(parent, parentChar);
    }

    @Override
    public OptimizedLinkedPineNode obtainNextMarkedSuffix() {
        if (isRoot()) {
            return null;
        }
        if (nextMarkedSuffix == null) {
            OptimizedLinkedPineNode link = (OptimizedLinkedPineNode) obtainLink();
            if (link.isEndOfLine()) {
                nextMarkedSuffix = link;
            } else {
                nextMarkedSuffix = link.obtainNextMarkedSuffix();
            }
        }
        return nextMarkedSuffix;
    }

    @Override
    public List<OptimizedLinkedPineNode> listEndsOfLine() {
        List<OptimizedLinkedPineNode> endsOfLines = new LinkedList<>();
        if (isEndOfLine()) {
            endsOfLines.add(this);
        }
        OptimizedLinkedPineNode nextEndOfLine = obtainNextMarkedSuffix();
        while (nextEndOfLine != null) {
            endsOfLines.add(nextEndOfLine);
            nextEndOfLine = nextEndOfLine.obtainNextMarkedSuffix();
        }
        return endsOfLines;
    }

    @Override
    public LinkedPineNode makeBoundNode(IPineNode parent, char parentChar) {
        return new OptimizedLinkedPineNode(parent, parentChar);
    }

    @Override
    public LinkedPineNode makeNode() {
        return new OptimizedLinkedPineNode();
    }
}
