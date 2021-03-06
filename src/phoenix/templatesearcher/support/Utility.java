package phoenix.templatesearcher.support;

import phoenix.templatesearcher.api.IMatrix2D;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class Utility {
    public static final char[] ALPHABET =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static Random random = new Random();

    static {
    /*
     * When some test cannot not be passed:
	 * 1) uncomment the following output to System.out
	 * 2) wait for easy-checkable tests and copy the seed from the output.
	 * 3) set the seed here manually and comment seed randomizing
	 * 4) run test
	 */

        //        final int seed = -1649474505;
        final int seed = random.nextInt();
        random.setSeed(seed);
        System.out.println("[DEBUG]: random seed = " + seed);
    }

    public static int randomInt(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    public static int randomNonZeroCount(int maxCount) {
        return randomInt(1, maxCount);
    }

    public static String randomString(int length) {
        char[] chars = new char[length];

        for (int i = 0; i < length; i++) {
            chars[i] = ALPHABET[randomInt(0, ALPHABET.length - 1)];
        }

        return new String(chars);
    }

    public static Iterable<Character> iterableThroughRow (IMatrix2D matrix, int rowID, int begin, int end) {
        return () -> new Iterator<Character>() {
            int index = begin;

            @Override
            public boolean hasNext() {
                return index < end;
            }

            @Override
            public Character next() {
                if (index == end) {
                    throw new NoSuchElementException("Cannot go out of the border");
                }
                return matrix.charAt(index++, rowID);
            }
        };
    }

    public static Iterable<Character> iterableThroughRow(IMatrix2D matrix, int rowID) {
        return iterableThroughRow(matrix, rowID, 0, matrix.getWidth());
    }
}
