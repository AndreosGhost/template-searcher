package phoenix.templatesearcher.highloadtests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import phoenix.templatesearcher.algo.api.ICharComparator;
import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.matchers.NaiveTemplateMatcher;
import phoenix.templatesearcher.support.ReadOnlyPair;
import phoenix.templatesearcher.test.matchers.IMatcherTest;
import phoenix.templatesearcher.test.matchers.MatcherTester;
import phoenix.templatesearcher.test.matchers.MatcherTester.StringSupplier;

import java.util.Random;

import static phoenix.templatesearcher.support.Utility.*;

@RunWith(JUnit4.class)
public class NaiveTemplateMatcherTestHighLoad implements IMatcherTest {
    /**
     * Basic number for all tests with local random.
     */
    public static final int LOCAL_RANDOM_BASE = randomInt(0, 99999999);

    private static final int TEMPLATE_SIZE = 1000;

    private static final int STREAM_SIZE = 10000;

    /**
     * Generate a line for chars with given integer probabilities among them.<br/>
     * Generates equal lines for equal the same chars and probabilities arrays.
     * @param chars
     * @param probabilities
     * @return Line generator that takes line size as parameter.
     */
    protected static StringSupplier obtainSeveralCharsStringSupplier(char[] chars, int[] probabilities) {
        if (chars.length != probabilities.length) {
            throw new IllegalArgumentException("chars length must be equal to probabilities length");
        }

        int[] probPrefixes = new int[probabilities.length];
        probPrefixes[0] = probabilities[0];

        for (int i = 1; i < probabilities.length; i++) {
            probPrefixes[i] = probabilities[i] + probPrefixes[i - 1];
        }

        int probabilitiesSum = probPrefixes[probPrefixes.length - 1];

        return (size) -> {
            char[] data = new char[size];
            Random random = new Random(size * LOCAL_RANDOM_BASE);

            for (int index = 0; index < size; index++) {
                int probCase = random.nextInt() % probabilitiesSum + 1;
                for (int i = 0; i < probabilities.length; i++) {
                    if (probPrefixes[i] >= probCase) {
                        data[index] = chars[i];
                        break;
                    }
                }
            }

            return new String(data);
        };
    }

    @Override
    public IMetaTemplateMatcher obtainFreshMatcher() {
        return new NaiveTemplateMatcher();
    }

    @Override
    public ICharComparator obtainCharComparator() {
        return ICharComparator.DEFAULT_COMPARATOR;
    }

    @Test
    public void testOneTemplateWithEqualSymbolsAndStreamWithEqualSymbols() {
        StringSupplier supplier =
                StringSupplier.singleCharStringSupplier(ALPHABET[randomInt(0, ALPHABET.length - 1)]);
        ReadOnlyPair<String, String[]> data = MatcherTester.makeTestData(
                1, 1, TEMPLATE_SIZE, TEMPLATE_SIZE, STREAM_SIZE, STREAM_SIZE, supplier, supplier);
        performTest(data);
    }

    @Test
    public void testSeveralTemplatesWithEqualSymbolsAndStreamWithEqualSymbols() {
        StringSupplier supplier = StringSupplier.singleCharStringSupplier(
                ALPHABET[randomInt(0, ALPHABET.length - 1)]);
        ReadOnlyPair<String, String[]> data = MatcherTester.makeTestData(
                10, 10, TEMPLATE_SIZE, TEMPLATE_SIZE, STREAM_SIZE, STREAM_SIZE, supplier, supplier);
        performTest(data);
    }

    @Test
    public void testOneTemplateWithSeveralSymbolsAndStreamWithSeveralSymbols() {
        StringSupplier supplier =
                obtainSeveralCharsStringSupplier("abcd".toCharArray(), new int[] {1, 2, 4, 5});
        ReadOnlyPair<String, String[]> data = MatcherTester.makeTestData(
                1, 1, TEMPLATE_SIZE, TEMPLATE_SIZE, STREAM_SIZE, STREAM_SIZE, supplier, supplier);
        performTest(data);
    }

    @Test
    public void testSeveralTemplatesWithSeveralSymbolsAndStreamWithSeveralSymbols() {
        StringSupplier supplier =
                obtainSeveralCharsStringSupplier("abcd".toCharArray(), new int[] {1, 2, 4, 5});
        ReadOnlyPair<String, String[]> data = MatcherTester.makeTestData(
                10, 10, TEMPLATE_SIZE, TEMPLATE_SIZE, STREAM_SIZE, STREAM_SIZE, supplier, supplier);
        performTest(data);
    }

    protected void performTest(ReadOnlyPair<String, String[]> testData) {
        MatcherTester.testMatchStream(obtainFreshMatcher(), testData, obtainCharComparator());
    }
}
