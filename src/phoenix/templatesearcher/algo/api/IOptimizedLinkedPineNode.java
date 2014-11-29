package phoenix.templatesearcher.algo.api;

import java.util.List;

/**
 * Linked pine node which stores also a link to the largest suffix marked as end of line.
 */
public interface IOptimizedLinkedPineNode<Node extends IOptimizedLinkedPineNode<Node>>
        extends IPineNode<Node> {
    /**
     * Obtains (gets or counts) the largest suffix of the line associated with this node that is
     * added as line to the pine forest.
     * @return Link to node or null, if not found.
     */
    public Node obtainNextMarkedSuffix();

    /**
     * Obtains all suffixes of the line associated with this node that are added as lines to the
     * pine forest.
     * @return All occurrences or empty list.
     */
    public List<Node> listEndsOfLine();
}
