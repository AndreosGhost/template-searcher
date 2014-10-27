package phoenix.templatesearcher.algo;

/**
 * A pine node with suffix links.
 */
public class LinkedPineNode extends SimplePineNode {
    private LinkedPineNode link;

    public LinkedPineNode() {
        super();
    }

    public LinkedPineNode(IPineNode parent, char parentChar) {
        super(parent, parentChar);
    }

    /**
     * Obtains (gets or generates) suffix link of this node.<br/>
     * Suffix link is a link to the node at which the largest own suffix of the line associated
     * with
     * this node ends.
     * @return
     */
    protected LinkedPineNode obtainLink() {
        if (link == null) {
            LinkedPineNode parent = (LinkedPineNode) getParent();
            if (isRoot() || parent.isRoot()) {
                link = (isRoot() ? this : parent);
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
    public LinkedPineNode go(char symbol) {
        SimplePineNode next = super.go(symbol);
        if (next == null) {
            if (isRoot()) {
                return this;
            } else {
                return obtainLink().go(symbol);
            }
        } else {
            return (LinkedPineNode) next;
        }
    }

    @Override
    public LinkedPineNode makeNode() {
        return new LinkedPineNode();
    }

    @Override
    public LinkedPineNode makeBoundNode(IPineNode parent, char parentChar) {
        return new LinkedPineNode(parent, parentChar);
    }
}
