package phoenix.templatesearcher.algo.api;

public interface IPineNodeVisitor<V extends IPineNode> {
    public void visit(V node);
}