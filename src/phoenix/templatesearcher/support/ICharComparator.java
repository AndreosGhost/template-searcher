package phoenix.templatesearcher.support;

/**
 * Basic interface for character comparison.
 * @author phoenix
 */
@FunctionalInterface
public interface ICharComparator {
    /**
     * Special symbol for templates compared by {@link #WILDCARD_COMPARATOR} that is equal to any other
     * symbol.<br/>
     * Value of this symbol: {@code ?}.
     */
    public static final char WILDCARD_SYMBOL = '?';

    /**
     * Special symbol for all comparators declared in {@link phoenix.templatesearcher.support.ICharComparator}
     * that is equal to no other symbol even itself.<br/>
     * Value of this symbol: {@code \u0000}.
     */
    public static final char SERVICE_SYMBOL = '\u0000';

    /**
     * Char comparison implementation that handles wildcard symbol {@link #WILDCARD_SYMBOL}.
     */
    public static final ICharComparator WILDCARD_COMPARATOR = (a, b) -> {
        if (a == SERVICE_SYMBOL || b == SERVICE_SYMBOL) {
            return false;
        }
        return a == WILDCARD_SYMBOL || b == WILDCARD_SYMBOL || a == b;
    };

    /**
     * Default implementation that uses '==' comparison.
     */
    public static final ICharComparator DEFAULT_COMPARATOR = (a, b) -> !(a == SERVICE_SYMBOL || b == SERVICE_SYMBOL) && a == b;

    boolean areEqual(char a, char b);
}