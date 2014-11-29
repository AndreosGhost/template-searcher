package phoenix.templatesearcher.algo;

import phoenix.templatesearcher.algo.api.IOptimizedLinkedPineNode;

import java.util.List;

/**
 * Convenience class that joins functionality of {@link phoenix.templatesearcher.algo.PineForest}
 * and {@link phoenix.templatesearcher.algo.flexible.OptimizedLinkedPineNode}
 */
public final class FinalOptimizedLinkedPineForest extends PineForest<FinalOptimizedLinkedPineNode>
        implements IOptimizedLinkedPineNode<FinalOptimizedLinkedPineNode> {
    public FinalOptimizedLinkedPineForest() {
        super(new FinalOptimizedLinkedPineNode());
    }

    @Override
    public FinalOptimizedLinkedPineNode obtainNextMarkedSuffix() {
        return root.obtainNextMarkedSuffix();
    }

    @Override
    public List<FinalOptimizedLinkedPineNode> listEndsOfLine() {
        return root.listEndsOfLine();
    }
}
