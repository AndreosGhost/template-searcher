package phoenix.templatesearcher.support;

import phoenix.templatesearcher.api.IMatrix2D;

import java.util.Objects;

/**
 * Implementation of two dimensional matrix of characters.
 */
public class Matrix2D implements IMatrix2D {
    @Override
    public IMatrix2D subMatrix(int xTopLeft, int yTopLeft, int width, int height) {
        checkIndexes(xTopLeft, yTopLeft, this.width, this.height);
        checkIndexes(xTopLeft + width, yTopLeft + height, this.width, this.height);

        IMatrix2D hostMatrix = this;

        return new IMatrix2D() {
            final IMatrix2D host = hostMatrix;

            @Override
            public char charAt(int x, int y) throws IndexOutOfBoundsException {
                checkIndexes(x, y, width, height);
                return host.charAt(x + xTopLeft, y + yTopLeft);
            }

            @Override
            public int getWidth() {
                return width;
            }

            @Override
            public int getHeight() {
                return height;
            }

            @Override
            public IMatrix2D subMatrix(int xTopLeftSub, int yTopLeftSub, int widthSub, int heightSub) {
                return host.subMatrix(xTopLeftSub + xTopLeft, yTopLeftSub + yTopLeft, widthSub, heightSub);
            }

            @Override
            public boolean equals(Object obj) {
                return areEqual(this, obj);
            }

            @Override
            public int hashCode() {
                return Matrix2D.hashCode(this);
            }

            @Override
            public String toString() {
                return Matrix2D.toString(this);
            }
        };
    }

    /**
     * matrix[x][y] is a character in row #y, column #x.
     */
    private final char[][] matrix;

    /**
     * Width of the matrix (maximal x value).
     */
    private final int width;

    /**
     * Height of the matrix (maximal y value).
     */
    private final int height;

    /**
     * Creates a two-dimensional matrix over the array of strings.<br/>
     * The created object doesn't depend on further changes on the given matrix object.
     * @param matrix
     *         Array of strings presenting matrix. All strings must be of equal length.
     * @throws NullPointerException
     *         If matrix, or one of its rows is null.
     * @throws IllegalArgumentException
     *         If matrix or one of its rows is empty or there are rows of different length.
     */
    public Matrix2D(String... matrix) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(matrix, "Matrix must not be null");
        height = matrix.length;
        if (height == 0) {
            throw new IllegalArgumentException("Matrix must have a positive height");
        }

        Objects.requireNonNull(matrix[0], "Matrix cannot have null rows");
        width = matrix[0].length();
        if (width == 0) {
            throw new IllegalArgumentException("Matrix must have a positive width");
        }

        for (int i = 1; i < height; i++) {
            Objects.requireNonNull(matrix[i], "Matrix cannot have null rows");
            if (matrix[i].length() != width) {
                throw new IllegalArgumentException("Matrix must not have rows of different length");
            }
        }

        this.matrix = new char[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.matrix[x][y] = matrix[y].charAt(x);
            }
        }
    }

    protected final static void checkIndexes(int x, int y, int width, int height) {
        if (x < 0 || x > width || y < 0 || y > height) {
            throw new IndexOutOfBoundsException(
                    "Bounds of any matrix are: 0 <= x <= getWidth(), 0 <= y <= getHeight()");
        }
    }

    @Override
    public char charAt(int x, int y) {
        checkIndexes(x, y, width, height);

        return matrix[x][y];
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    protected static boolean areEqual(IMatrix2D matrixA, Object obj) {
        if (!(obj instanceof IMatrix2D)) {
            return false;
        }
        IMatrix2D matrixB = (IMatrix2D)obj;

        if (matrixA.getWidth() != matrixB.getWidth() || matrixA.getHeight() != matrixB.getHeight()) {
            return false;
        }

        for (int x = 0; x < matrixA.getWidth(); x++) {
            for (int y = 0; y < matrixA.getHeight(); y++) {
                if (matrixA.charAt(x, y) != matrixB.charAt(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

    protected static int hashCode (IMatrix2D matrix) {
        return Objects.hash(matrix.getWidth(), matrix.getHeight());
    }

    protected static String toString (IMatrix2D matrix) {
        StringBuilder sb = new StringBuilder(matrix.getHeight() * (matrix.getWidth() + 1));
        for (int row = 0; row < matrix.getHeight(); row++) {
            for (int col = 0; col < matrix.getWidth(); col++) {
                sb.append(matrix.charAt(col, row));
            }
            sb.append("|");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return toString(this);
    }

    @Override
    public int hashCode() {
        return hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return areEqual(this, obj);
    }
}
