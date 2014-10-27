package phoenix.templatesearcher.algo;

import java.util.List;

/**
 * Linked pine node which stores also a link to the largest suffix marked as end of line.
 */
public interface IOptimizedLinkedPineNode extends IPineNode {
    /**
     * Obtains (gets or counts) the largest suffix of the line associated with this node that is
     * added as line to the pine forest.
     * @return
     */
    public IOptimizedLinkedPineNode obtainNextMarkedSuffix();

    /**
     * Obtains all suffixes of the line associated with this node that are added as lines to the
     * pine forest.
     * @return
     */
    public List<? extends IOptimizedLinkedPineNode> listEndsOfLine();
}
