package phoenix.templatesearcher.algo;

import phoenix.templatesearcher.support.ICharComparator;

/**
 * Class for counting prefix function
 * @author phoenix
 */
public class PrefixFunction {

    /**
     * Counts prefix function for a substring
     * @param data
     *         Array of characters
     * @param pi
     *         Prefix function array
     * @param begin
     *         First index (inclusive)
     * @param end
     *         Last index (exclusive)
     * @param comparator
     *         Provider for character comparison
     */
    public static void countPrefixFunction(char[] data,
                                           int[] pi,
                                           int begin,
                                           int end,
                                           ICharComparator comparator) {
        if (begin == 0) {
            pi[0] = 0;
            begin++;
        }
        for (int j = begin; j < end; j++) {
            int amortizedLength = pi[j - 1];

            while (amortizedLength > 0 && !comparator.areEqual(data[j], data[amortizedLength])) {
                amortizedLength = pi[amortizedLength - 1];
            }
            if (comparator.areEqual(data[j], data[amortizedLength])) {
                amortizedLength++;
            }
            pi[j] = amortizedLength;
        }
    }

    public static void countPrefixFunction(char[] data, int[] pi, int begin, int end) {
        countPrefixFunction(data, pi, begin, end, ICharComparator.DEFAULT_COMPARATOR);
    }

    public static void countPrefixFunction(char[] data, int[] pi) {
        countPrefixFunction(data, pi, 0, data.length, ICharComparator.DEFAULT_COMPARATOR);
    }

    public static void countPrefixFunction(char[] data, int[] pi, ICharComparator comparator) {
        countPrefixFunction(data, pi, 0, data.length, comparator);
    }
}
