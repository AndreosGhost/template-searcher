package phoenix.templatesearcher.algo.flexible;

import phoenix.templatesearcher.algo.api.IPineNode;
import phoenix.templatesearcher.algo.api.IPineNodeVisitor;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple implementation if {@link phoenix.templatesearcher.algo.api.IPineNode} interface.
 */
public abstract class PineNode<Node extends PineNode<Node>> implements IPineNode<Node> {

    private static int ID_COUNTER = 0;
    private int id;
    private int endOfLineID;
    private Map<Character, Node> nextNodes;
    private Node parent;
    private char parentChar;

    public PineNode() {
        this.id = ID_COUNTER++;
        this.endOfLineID = NOT_END_OF_LINE_ID;
    }

    public PineNode(Node parent, char parentChar) {
        this();
        this.parent = parent;
        this.parentChar = parentChar;
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
    public void setEndOfLine(int lineID) {
        this.endOfLineID = lineID;
    }

    @Override
    public int getEndOfLineID() {
        return endOfLineID;
    }

    @Override
    public Node go(char symbol) {
        if (nextNodes == null || !nextNodes.containsKey(symbol)) {
            return null;
        }
        return nextNodes.get(symbol);
    }

    @Override
    public Node add(char symbol) {
        if (nextNodes == null) {
            nextNodes = new HashMap<>();
        }
        Node next = nextNodes.get(symbol);
        if (next == null) {
            next = makeBoundNode(thisNode(), symbol);
            nextNodes.put(symbol, next);
        }
        return next;
    }

    @Override
    public Node getParent() {
        return parent;
    }

    @Override
    public void walkSubtree(IPineNodeVisitor<IPineNode<Node>> visitor) {
        ArrayDeque<PineNode<Node>> visitPlan = new ArrayDeque<>();
        visitPlan.add(this);

        while (!visitPlan.isEmpty()) {
            PineNode<Node> node = visitPlan.pollFirst();
            visitor.visit(node);
            if (node.nextNodes != null) {
                visitPlan.addAll(node.nextNodes.values());
            }
        }
    }

    @Override
    public boolean isRoot() {
        return parent == null;
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
    public char getParentChar() {
        return parentChar;
    }

    @Override
    public String toString() {
        return String.format("%d: %s%s", getID(), getLine(), isEndOfLine() ? getEndOfLineID() : "");
    }
}
