package phoenix.templatesearcher.algo;

import java.util.List;

/**
 * Convenience class that joins functionality of {@link phoenix.templatesearcher.algo.PineForest}
 * and {@link phoenix.templatesearcher.algo.OptimizedLinkedPineNode}
 */
public class OptimizedLinkedPineForest extends PineForest<OptimizedLinkedPineNode>
        implements IOptimizedLinkedPineNode {
    public OptimizedLinkedPineForest() {
        super(new OptimizedLinkedPineNode());
    }

    @Override
    public IOptimizedLinkedPineNode obtainNextMarkedSuffix() {
        return root.obtainNextMarkedSuffix();
    }

    @Override
    public List<OptimizedLinkedPineNode> listEndsOfLine() {
        return root.listEndsOfLine();
    }
}
