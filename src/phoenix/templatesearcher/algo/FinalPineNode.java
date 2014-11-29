package phoenix.templatesearcher.algo;

import phoenix.templatesearcher.algo.flexible.PineNode;

/**
 * Final adoptation of {@link phoenix.templatesearcher.algo.flexible.PineNode}.
 */
public final class FinalPineNode extends PineNode<FinalPineNode> {
    public FinalPineNode() {
    }

    public FinalPineNode(FinalPineNode parent, char parentChar) {
        super(parent, parentChar);
    }

    @Override
    public FinalPineNode makeBoundNode(FinalPineNode parent, char parentChar) {
        return new FinalPineNode(parent, parentChar);
    }

    @Override
    public FinalPineNode makeNode() {
        return new FinalPineNode();
    }

    @Override
    public FinalPineNode thisNode() {
        return this;
    }
}
