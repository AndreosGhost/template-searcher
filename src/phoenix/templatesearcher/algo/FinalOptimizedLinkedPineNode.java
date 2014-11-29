package phoenix.templatesearcher.algo;

import phoenix.templatesearcher.algo.flexible.OptimizedLinkedPineNode;

/**
 * Ready for use adaptation of {@link phoenix.templatesearcher.algo.flexible.OptimizedLinkedPineNode}.
 */
public final class FinalOptimizedLinkedPineNode
        extends OptimizedLinkedPineNode<FinalOptimizedLinkedPineNode> {

    public FinalOptimizedLinkedPineNode() {
    }

    public FinalOptimizedLinkedPineNode(FinalOptimizedLinkedPineNode parent, char parentChar) {
        super(parent, parentChar);
    }

    @Override
    public FinalOptimizedLinkedPineNode makeBoundNode(FinalOptimizedLinkedPineNode parent, char parentChar) {
        return new FinalOptimizedLinkedPineNode(parent, parentChar);
    }

    @Override
    public FinalOptimizedLinkedPineNode makeNode() {
        return new FinalOptimizedLinkedPineNode();
    }

    @Override
    public FinalOptimizedLinkedPineNode thisNode() {
        return this;
    }
}
