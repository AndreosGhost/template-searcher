package phoenix.templatesearcher.algo;

import phoenix.templatesearcher.exception.DuplicateLineException;
import phoenix.templatesearcher.support.IdentifiedLine;

import java.util.LinkedList;
import java.util.List;

public class PineForest<PineNode extends IPineNode> implements IPineNode, IPineForest {
    protected final PineNode root;

    @Override
    public char getParentChar() {
        return root.getParentChar();
    }

    @Override
    public boolean isRoot() {
        return root.isRoot();
    }

    @Override
    public String getLine() {
        return root.getLine();
    }

    @Override
    public int getID() {
        return root.getID();
    }

    @Override
    public boolean isEndOfLine() {
        return root.isEndOfLine();
    }

    @Override
    public void setEndOfLine(int lineID) {
        root.setEndOfLine(lineID);
    }

    @Override
    public int getEndOfLineID() {
        return root.getEndOfLineID();
    }

    @Override
    public IPineNode go(char symbol) {
        return root.go(symbol);
    }

    @Override
    public IPineNode add(char symbol) throws IllegalArgumentException {
        return root.add(symbol);
    }

    @Override
    public IPineNode makeBoundNode(IPineNode parent, char parentChar) {
        return root.makeBoundNode(parent, parentChar);
    }

    @Override
    public IPineNode makeNode() {
        return root.makeNode();
    }

    @Override
    public IPineNode getParent() {
        return root.getParent();
    }

    @Override
    public void walkSubtree(IPineNodeVisitor<IPineNode> visitor) {
        root.walkSubtree(visitor);
    }

    public PineForest(PineNode root) {
        if (!root.isRoot()) {
            throw new IllegalArgumentException("Given node is not a root");
        }
        this.root = root;
    }

    @Override
    public void addLine(char[] data, int lineID)
            throws IllegalArgumentException, DuplicateLineException {
        if (lineID == IPineNode.NOT_END_OF_LINE_ID) {
            throw new IllegalArgumentException("Unsupported line ID given");
        }

        IPineNode node = root;

        for (char c : data) {
            node = node.add(c);
        }
        if (node.isEndOfLine()) {
            throw new DuplicateLineException("Line has been already added: " + new String(data));
        }
        node.setEndOfLine(lineID);
    }

    @Override
    public List<IdentifiedLine> listIdentifiedLines() {
        LinesExtractor extractor = new LinesExtractor();
        root.walkSubtree(extractor);
        return extractor.getLines();
    }

    @Override
    public String[] listLines() throws IllegalStateException {
        List<IdentifiedLine> identifiedLines = listIdentifiedLines();
        String[] array = new String[identifiedLines.size()];

        for (IdentifiedLine line : identifiedLines) {
            if (line.getID() < 0 || line.getID() > array.length) {
                throw new IllegalStateException("Improper ids found");
            }
            if (array[line.getID()] != null) {
                throw new IllegalStateException(
                        "At least two different templates have equal id. Implementation error.");
            }
            array[line.getID()] = line.getLine();
        }

        return array;
    }

    /**
     * Class for walking pine forest and pulling lines from it.
     */
    private class LinesExtractor implements IPineNodeVisitor<IPineNode> {
        private final List<IdentifiedLine> lines = new LinkedList<>();

        public List<IdentifiedLine> getLines() {
            return lines;
        }

        @Override
        public void visit(IPineNode node, StringBuilder line) {
            if (node.isEndOfLine()) {
                lines.add(new IdentifiedLine(line.toString(), node.getEndOfLineID()));
            }
        }
    }
}
