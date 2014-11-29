package phoenix.templatesearcher.algo.api;

import phoenix.templatesearcher.exception.DuplicateLineException;
import phoenix.templatesearcher.support.IdentifiedLine;

import java.util.List;

/**
 * Base interface for forest
 */
public interface IPineForest {
    /**
     * Adds line to the pine forest char-by-char and marks the node associated with the new line as
     * the end of line.
     * @param line
     *         Line to add
     * @param lineID
     *         Identity number of the line.
     * @throws DuplicateLineException
     *         If the same line has been already added.
     * @throws IllegalArgumentException
     *         If unsupported line ID given.
     */
    public void addLine(char[] line, int lineID) throws DuplicateLineException, IllegalArgumentException;

    /**
     * Adds line to the pine forest char-by-char and marks the node associated with the new line as
     * the end of line.
     * @param line
     *         Line to add
     * @param lineID
     *         Identity number of the line.
     * @throws DuplicateLineException
     *         If the same line has been already added.
     * @throws IllegalArgumentException
     *         If unsupported line ID given.
     */
    public void addLine(Iterable<Character> line, int lineID) throws IllegalArgumentException, DuplicateLineException;

    /**
     * Collects lines officially added to this pine forest and puts them into array.
     * @return
     * @throws IllegalStateException
     *         if at least two different lines have same identity numbers or some lines have
     *         identity numbers not in range {@code [0; lines_count - 1]}.
     */
    public String[] listLines() throws IllegalStateException;

    /**
     * Walks over the whole pine forest and collects all lines officially added to the pine forest.
     * @return
     */
    public List<IdentifiedLine> listIdentifiedLines();
}
