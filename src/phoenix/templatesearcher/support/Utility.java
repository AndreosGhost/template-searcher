package phoenix.templatesearcher.support;

import java.util.Random;

public class Utility {
    private static Random random = new Random();

    static {
	/*
	 * When some tests are not passed:
	 * 1) put @Ignore on all test classes expect problematic one (if testing the whole package)
	 * 2) uncomment the following output to System.out
	 * 3) wait for easy-checkable tests and copy the seed from the output.
	 * 4) set the seed here manually and comment seed randomizing
	 * 5) run tests
	 */

	// final int seed = -1222781664;
	final int seed = random.nextInt();
	random.setSeed(seed);
	// System.out.println("[DEBUG]: random seed = " + seed);
    }

    private final static char[] alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
	    .toCharArray();

    public static int randomInt(int min, int max) {
	return min + random.nextInt(max - min + 1);
    }

    public static int randomNonZeroCount(int maxCount) {
	return randomInt(1, maxCount);
    }

    public static String randomString(int length) {
	char[] chars = new char[length];

	for (int i = 0; i < length; i++) {
	    chars[i] = alphabet[randomInt(0, alphabet.length - 1)];
	}

	return new String(chars);
    }

    /**
     * Counts prefix function for a substring
     * @param data array of characters
     * @param pi prefix function array
     * @param begin first index (inclusive)
     * @param end last index (exclusive)
     */
    public static void recountPrefixFunction(char[] data, int[] pi, int begin,
	    int end) {
	if (begin == 0) {
	    pi[0] = 0;
	    begin++;
	}
	for (int j = begin; j < end; j++) {
	    int amortizedLength = pi[j - 1];

	    while (amortizedLength > 0 && data[j] != data[amortizedLength]) {
		amortizedLength = pi[amortizedLength - 1];
	    }
	    if (data[j] == data[amortizedLength]) {
		amortizedLength++;
	    }
	    pi[j] = amortizedLength;
	}
    }
}
