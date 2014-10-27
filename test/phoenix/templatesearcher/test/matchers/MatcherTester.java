package phoenix.templatesearcher.test.matchers;

import junit.framework.AssertionFailedError;
import org.junit.runners.Parameterized;
import phoenix.templatesearcher.algo.PineForest;
import phoenix.templatesearcher.algo.SimplePineNode;
import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.api.IOccurrence;
import phoenix.templatesearcher.exception.DuplicateLineException;
import phoenix.templatesearcher.support.ICharComparator;
import phoenix.templatesearcher.support.Occurrence;
import phoenix.templatesearcher.support.ReadOnlyPair;
import phoenix.templatesearcher.test.support.StringCharStream;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static phoenix.templatesearcher.support.Utility.*;

/**
 * Convenience class for generating random tests and checking results on them.
 */
public final class MatcherTester {
    /**
     * Not for constructing
     */
    private MatcherTester() {

    }

    /**
     * Generates a random test according to given parameter boundaries.<br/>
     * It is not guaranteed that templates count will be greater than {@code templatesMinCount}
     * because of difficulties connected with random generation of unique strings.
     * @param templatesMinCount
     *         minimal count of templates
     * @param templatesMaxCount
     *         maximal count of templates
     * @param minTemplateLength
     *         minimal length of each template
     * @param maxTemplateLength
     *         maximal length of each template
     * @param minStreamLength
     *         minimal length of the stream
     * @param maxStreamLength
     *         maximal length of the stream
     * @return a pair (stream, templates[])
     */
    public static ReadOnlyPair<String, String[]> makeTestData(int templatesMinCount,
                                                              int templatesMaxCount,
                                                              int minTemplateLength,
                                                              int maxTemplateLength,
                                                              int minStreamLength,
                                                              int maxStreamLength) {
        String stream = randomString(randomInt(minStreamLength, maxStreamLength));
        int templatesCount = randomInt(templatesMinCount, templatesMaxCount);

        PineForest<SimplePineNode> templatesForest = new PineForest<>(new SimplePineNode());

        /*
        The problem is that randomString() method called many times can generate equal lines.
        If duplicate line is attempted to be added, it is regenerated some times, but not many,
        because it would take very long long time.
         */
        int repeatsWithSameI = 0;

        int maxRepeatsWithSameI = 10;

        for (int i = 0; i < templatesCount; i++) {
            String template = randomString(randomInt(minTemplateLength, maxTemplateLength));
            try {
                templatesForest.addLine(template.toCharArray(), i);
                repeatsWithSameI = 0;
            } catch (DuplicateLineException exc) {
                if (repeatsWithSameI < maxRepeatsWithSameI) {
                    i--;
                    repeatsWithSameI++;
                } else {
                    repeatsWithSameI = 0;
                }
            }
        }

        return new ReadOnlyPair<>(stream, templatesForest.listLines());
    }

    private static int indexOf(String source,
                               String substr,
                               int fromIndex,
                               ICharComparator comparator) {
        for (int i = fromIndex, iLast = source.length() - substr.length(); i <= iLast; i++) {
            boolean matchFound = true;
            for (int srcInd = i, subInd = 0, subLen = substr.length(); subInd < subLen;
                 srcInd++, subInd++) {
                if (!(comparator.areEqual(source.charAt(srcInd), substr.charAt(subInd)))) {
                    matchFound = false;
                    break;
                }
            }

            if (matchFound) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Finds matches in the stream using {@link String } methods.
     * @param stream
     * @param templates
     * @return
     */
    public static List<IOccurrence> matchStreamVeryNaive(String stream,
                                                         ICharComparator comparator,
                                                         String... templates) {
        List<IOccurrence> results = new LinkedList<>();

        for (int id = 0, count = templates.length; id < count; id++) {
            String template = templates[id];

            int index = 0;

            while (true) {
                index = indexOf(stream, template, index, comparator);
                if (index >= 0) {
                    results.add(new Occurrence(index + template.length() - 1, id));
                    index++;
                } else {
                    break;
                }
            }
        }

        return results;
    }

    public static void addTestDataMultipleTemplates(Collection<Object[]> data, int testsCount) {
        for (int test = 0; test < testsCount; test++) {
            data.add(MatcherTester.wrapParameters(makeTestData(1, 10, 1, 20, 1, 100)));
        }
    }

    public static void addTestDataOneLongTemplateShortStream(Collection<Object[]> data,
                                                             int testsCount) {
        for (int test = 0; test < testsCount; test++) {
            data.add(
                    MatcherTester.wrapParameters(makeTestData(1, 1, 15, 20, 1, 30)));
        }
    }

    public static void addTestDataOneTemplate(Collection<Object[]> data, int testsCount) {
        for (int test = 0; test < testsCount; test++) {
            data.add(
                    MatcherTester.wrapParameters(makeTestData(1, 1, 1, 20, 1, 100)));
        }
    }

    /**
     * Checks whether two result sets are equal. If not, an {@link AssertionFailedError } is
     * thrown.
     * @param actual
     *         result set generated by the tested class
     * @param expected
     *         result set generated by our very naive method.
     * @throws AssertionFailedError
     *         if result sets are not equal (order is ignored)
     */
    public static void checkOccurrenceResults(List<IOccurrence> actual, List<IOccurrence> expected)
            throws AssertionFailedError {
        HashSet<IOccurrence> checkSet = new HashSet<>();

        checkSet.addAll(actual);

        for (IOccurrence occ : expected) {
            boolean removed = checkSet.remove(occ);
            if (!removed) {
                // not present
                throw new AssertionFailedError(
                        String.format(
                                "Expected element not present: %s;\nExpected set: %s;\nActual set: %s",
                                occ,
                                expected,
                                actual));
            }
        }

        if (!checkSet.isEmpty()) {
            throw new AssertionFailedError(
                    String.format(
                            "Extra elements found: %s;\nExpected set: %s;\nActual set: %s",
                            checkSet,
                            expected,
                            actual));
        }
    }

    /**
     * Parameters are wrapped into array ready to be given to {@link Parameterized } runner.
     * @param pair
     *         a pair (stream, templates[]).
     * @return an array { stream, templates[] }.
     */
    public static Object[] wrapParameters(ReadOnlyPair<String, String[]> pair) {
        return new Object[] {pair.getKey(), pair.getValue()};
    }

    /**
     * Performs a single test for the given matcher with given parameters and checks the results.
     * @param matcher
     *         Matcher to test.
     * @param stream
     *         Stream to test the matcher against.
     * @param templates
     *         Templates to search in the stream.
     * @param comparator
     *         Object used to distinguish characters.
     * @param addTemplatesToMatcher
     *         If true, templates are added using {@link phoenix.templatesearcher.api.IMetaTemplateMatcher#addTemplate(String)}
     *         method. Otherwise they are considered to be present there.
     */
    public static void testMatchStream(IMetaTemplateMatcher matcher,
                                       String stream,
                                       String[] templates,
                                       ICharComparator comparator,
                                       boolean addTemplatesToMatcher) {
        if (addTemplatesToMatcher) {
            for (String template : templates) {
                matcher.addTemplate(template);
            }
        }

        List<IOccurrence> actualResults = matcher.matchStream(new StringCharStream(stream));
        List<IOccurrence> expectedResults = matchStreamVeryNaive(
                stream, comparator, templates);

        try {
            checkOccurrenceResults(actualResults, expectedResults);
        } catch (AssertionFailedError ass) {
            System.err.println("Failure: " + ass.getMessage());
            System.err.println("Stream: " + stream);
            System.err.println("Templates: " + Arrays.toString(templates));
            throw ass;
        }
    }

    /**
     * Performs a single test with given parameters.<br/>
     * Equivalent to call {@code testMatchStream(matcher, stream, templates, comparator, true);}.
     * @param stream
     * @param templates
     */
    public static void testMatchStream(IMetaTemplateMatcher matcher,
                                       String stream,
                                       String[] templates,
                                       ICharComparator comparator) {
        testMatchStream(matcher, stream, templates, comparator, true);
    }

    //
    //    /**
    //     * Performs a single test with given parameters.<br/>
    //     * Equivalent to call {@code testMatchStream(matcher, stream, templates,
    //     *ICharComparator.DEFAULT_COMPARATOR, addTemplatesToMatcher);}.
    //     * @param stream
    //     * @param templates
    //     * @see phoenix.templatesearcher.support.ICharComparator#DEFAULT_COMPARATOR
    //     */
    //    public static void testMatchStream(IMetaTemplateMatcher matcher,
    //                                       String stream,
    //                                       String[] templates,
    //                                       boolean addTemplatesToMatcher) {
    //        testMatchStream(
    //                matcher,
    //                stream,
    //                templates,
    //                ICharComparator.DEFAULT_COMPARATOR,
    //                addTemplatesToMatcher);
    //    }
    //
    //    /**
    //     * Performs a single test with given parameters.<br/>
    //     * Equivalent to call {@code testMatchStream(matcher, stream, templates,
    //     *ICharComparator.DEFAULT_COMPARATOR, true);}.
    //     * @param stream
    //     * @param templates
    //     * @see phoenix.templatesearcher.support.ICharComparator#DEFAULT_COMPARATOR
    //     */
    //    public static void testMatchStream(IMetaTemplateMatcher matcher,
    //                                       String stream,
    //                                       String[] templates) {
    //        testMatchStream(matcher, stream, templates, ICharComparator.DEFAULT_COMPARATOR, true);
    //    }
}
