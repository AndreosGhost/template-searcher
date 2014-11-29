package phoenix.templatesearcher.algo.flexible;

/**
 * A pine node with suffix links.
 */
public abstract class LinkedPineNode<Node extends LinkedPineNode<Node>> extends PineNode<Node> {
    private Node link;

    public LinkedPineNode() {
        super();
    }

    public LinkedPineNode(Node parent, char parentChar) {
        super(parent, parentChar);
    }

    /**
     * Obtains (gets or generates) suffix link of this node.<br/>
     * Suffix link is a link to the node at which the largest own suffix of the line associated
     * with
     * this node ends.
     * @return
     */
    protected Node obtainLink() {
        if (link == null) {
            Node parent = getParent();
            if (isRoot() || parent.isRoot()) {
                link = (isRoot() ? thisNode() : parent);
            } else {
                link = parent.obtainLink().go(getParentChar());
            }
        }
        return link;
    }

    /**
     * {@inheritDoc}
     * If no transition through this symbol exists, an attempt to {@link #go(char)} is performed
     * over the suffix link of this node or, if this node is the root, nothing is done and this
     * node is returned.
     * @see #obtainLink()
     */
    @Override
    public Node go(char symbol) {
        Node next = super.go(symbol);
        if (next == null) {
            if (isRoot()) {
                return thisNode();
            } else {
                return obtainLink().go(symbol);
            }
        } else {
            return next;
        }
    }
}
