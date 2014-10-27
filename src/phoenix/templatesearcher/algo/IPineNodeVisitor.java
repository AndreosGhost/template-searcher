package phoenix.templatesearcher.algo;

public interface IPineNodeVisitor<V extends IPineNode> {
    public void visit(V node, StringBuilder line);
}