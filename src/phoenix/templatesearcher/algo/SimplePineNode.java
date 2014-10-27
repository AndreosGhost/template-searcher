package phoenix.templatesearcher.algo;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Simple implementation if {@link phoenix.templatesearcher.algo.IPineNode} interface.
 */
public class SimplePineNode implements IPineNode {

    private static int ID_COUNTER = 0;
    private int id;
    private int endOfLineID;
    private Map<Character, SimplePineNode> nextNodes;
    private IPineNode parent;
    private char parentChar;

    public SimplePineNode() {
        this.id = ID_COUNTER++;
        this.endOfLineID = NOT_END_OF_LINE_ID;
    }

    public SimplePineNode(IPineNode parent, char parentChar) {
        this();
        this.parent = parent;
        this.parentChar = parentChar;
    }

    @Override
    public boolean isRoot() {
        return parent == null;
    }

    @Override
    public char getParentChar() {
        return parentChar;
    }

    protected void walkSubtree(IPineNodeVisitor<IPineNode> visitor, StringBuilder line) {
        visitor.visit(this, line);
        if (nextNodes != null) {
            for (Entry<Character, SimplePineNode> entry : nextNodes.entrySet()) {
                line.append(entry.getKey());
                entry.getValue().walkSubtree(visitor, line);
                line.deleteCharAt(line.length() - 1);
            }
        }
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public boolean isEndOfLine() {
        return endOfLineID != NOT_END_OF_LINE_ID;
    }

    @Override
    public int getEndOfLineID() {
        return endOfLineID;
    }

    @Override
    public void setEndOfLine(int lineID) {
        this.endOfLineID = lineID;
    }

    @Override
    public SimplePineNode go(char symbol) {
        if (nextNodes == null || !nextNodes.containsKey(symbol)) {
            return null;
        }
        return nextNodes.get(symbol);
    }

    @Override
    public SimplePineNode add(char symbol) {
        if (nextNodes == null) {
            nextNodes = new HashMap<>();
        }
        SimplePineNode next = nextNodes.get(symbol);
        if (next == null) {
            next = makeBoundNode(this, symbol);
            nextNodes.put(symbol, next);
        }
        return next;
    }

    @Override
    public SimplePineNode makeBoundNode(IPineNode parent, char parentChar) {
        return new SimplePineNode(parent, parentChar);
    }

    @Override
    public SimplePineNode makeNode() {
        return new SimplePineNode();
    }

    @Override
    public IPineNode getParent() {
        return parent;
    }

    @Override
    public void walkSubtree(IPineNodeVisitor<IPineNode> visitor) {
        walkSubtree(visitor, new StringBuilder());
    }

    @Override
    public String getLine() {
        StringBuilder sb = new StringBuilder();
        IPineNode node = this;
        while (node.getParent() != null) {
            sb.append(node.getParentChar());
            node = node.getParent();
        }
        sb.reverse();
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("%d: %s%s", getID(), getLine(), isEndOfLine() ? getEndOfLineID() : "");
    }
}
