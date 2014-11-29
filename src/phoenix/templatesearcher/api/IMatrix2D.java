package phoenix.templatesearcher.api;

/**
 * Two-dimensional array of characters.
 */
public interface IMatrix2D {
    char charAt(int x, int y) throws IndexOutOfBoundsException;

    int getWidth();

    int getHeight();

    /**
     * Returns submatrix that is backed by this matrix (all changes in one of them cause the same changes in
     * the other).
     * @param xTopLeft
     *         x-coordinate of top left corner.
     * @param yTopLeft
     *         y-coordinate of top left corner.
     * @param width
     *         width of the submatrix.
     * @param height
     *         height of the submatrix.
     * @return submatrix.
     */
    IMatrix2D subMatrix(int xTopLeft, int yTopLeft, int width, int height) throws IndexOutOfBoundsException;

    /**
     * Checks this matrix and the given object for equality.<br/>
     * The contract is the following: if obj is instance of {@link phoenix.templatesearcher.api.IMatrix2D} it
     * is equal to this object only if both these objects present matrices of equal size and contain equal
     * characters on the same positions.
     * @param obj
     *         object to compare with.
     * @return true, if equal, false otherwise.
     */
    boolean equals(Object obj);
}
