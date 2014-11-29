package phoenix.templatesearcher.algo.api;

/**
 * Base interface for a node in a pine forest.<br/>
 * It is strongly guaranteed that all node creations in implementations of this interface are
 * performed through {@link #makeBoundNode(IPineNode, char)}} and {@link #makeNode()}.
 */
public interface IPineNode<Node extends IPineNode<Node>> {
    /**
     * Line ID that indicates that no line ends at this node.<br/>
     * Actual value: {@code -1}.
     */
    int NOT_END_OF_LINE_ID = -1;

    /**
     * Returns a unique identifier of this node.<br/>ID is given when a node is constructed.
     * @return non-negative integer.
     */
    int getID();

    /**
     * Returns whether some line added to the host pine forest ends at this node.<br/>
     * @return
     */
    boolean isEndOfLine();

    /**
     * Marks this node as end of the line constructed of transition characters on edges from the
     * root to this node.
     * @param lineID
     *         identifier of the line.
     */
    void setEndOfLine(int lineID);

    /**
     * Returns the identifier of line that ends at this node or {@link #NOT_END_OF_LINE_ID}.
     * @return
     */
    int getEndOfLineID();

    /**
     * Attempts to go to the next node using given symbol.
     * @param symbol
     *         Transition character.
     * @return Next node or {@code null} if this symbol has not been added.
     */
    Node go(char symbol);

    /**
     * Adds a character transition to this node.
     * @param symbol
     *         Transition character.
     * @return Next node, that can be reached by the given character from this node.
     */
    Node add(char symbol);

    /**
     * Returns a new instance of the current implementation of the interface {@link
     * IPineNode}.<br/>
     * The {@code parent} object is not changed.
     * @param parent
     *         Parent node.
     * @param parentChar
     *         Character that is a bridge from parent node to the new node.
     * @return A new node.
     */
    Node makeBoundNode(Node parent, char parentChar);

    /**
     * Returns a new instance of the current implementation of the interface {@link
     * IPineNode}.
     * @return
     */
    Node makeNode();

    Node thisNode();

    /**
     * Returns parent of this node.
     * @return Parent of this node or {@code null} if this node is root.
     */
    Node getParent();

    /**
     * Performs a deep-find-search over the subtree of this node.
     * @param visitor
     *         Some class that will be informed when a node is visited.
     */
    void walkSubtree(IPineNodeVisitor<IPineNode<Node>> visitor);

    /**
     * Returns whether this node is root node.
     * @return
     */
    boolean isRoot();

    /**
     * Constructs a line of transition characters from root to this node.
     * @return
     */
    String getLine();

    /**
     * Returns transition character which leads from this node's parent to this node.
     * @return
     */
    char getParentChar();
}
