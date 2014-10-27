package phoenix.templatesearcher.support;

/**
 * Basic interface for character comparison.
 * @author phoenix
 */
public interface ICharComparator {
    public static final char WILDCARD_SYMBOL = '?';

    public static final char SERVICE_SYMBOL = '\u0000';

    /**
     * Wildcard symbol {@link #WILDCARD_SYMBOL} is equal to any other symbol.
     */
    public static final ICharComparator WILDCARD_COMPARATOR = new ICharComparator() {
        @Override
        public boolean areEqual(char a, char b) {
            if (a == SERVICE_SYMBOL || b == SERVICE_SYMBOL) {
                return false;
            }
            if (a == WILDCARD_SYMBOL || b == WILDCARD_SYMBOL) {
                return true;
            }
            return a == b;
        }
    };

    /**
     * Default implementation that uses '==' comparison.
     */
    public static final ICharComparator DEFAULT_COMPARATOR = new ICharComparator() {
        @Override
        public boolean areEqual(char a, char b) {
            if (a == SERVICE_SYMBOL || b == SERVICE_SYMBOL) {
                return false;
            }
            return a == b;
        }
    };

    boolean areEqual(char a, char b);
}