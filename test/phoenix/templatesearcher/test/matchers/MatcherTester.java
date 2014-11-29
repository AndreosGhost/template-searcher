package phoenix.templatesearcher.test.matchers;

import junit.framework.AssertionFailedError;
import org.junit.runners.Parameterized;
import phoenix.templatesearcher.algo.FinalPineNode;
import phoenix.templatesearcher.algo.PineForest;
import phoenix.templatesearcher.algo.api.ICharComparator;
import phoenix.templatesearcher.api.IMatrix2D;
import phoenix.templatesearcher.api.IMetaTemplate2DMatcher;
import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.api.IOccurrence;
import phoenix.templatesearcher.api.IOccurrence2D;
import phoenix.templatesearcher.exception.DuplicateLineException;
import phoenix.templatesearcher.support.Matrix2D;
import phoenix.templatesearcher.support.Occurrence;
import phoenix.templatesearcher.support.Occurrence2D;
import phoenix.templatesearcher.support.ReadOnlyPair;
import phoenix.templatesearcher.support.Utility;
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
     * Interface for character row generators.
     */
    @FunctionalInterface
    public interface RowSupplier {
        /**
         * Supplies string for the given row id that must have strictly {@code length} characters.
         * @param rowID
         *         row id. You can ignore this.
         * @param length
         *         length of string that must be returned.
         * @return string of the given length.
         */
        String supplyRow(int rowID, int length);

        /**
         * Supplier for completely random strings.
         */
        static RowSupplier randomRowSupplier() {
            return (rowID, length) -> Utility.randomString(length);
        }

        /**
         * Supplier for strings that contain only one character n times.
         */
        static RowSupplier singleCharRowSupplier(char c) {
            return (rowID, length) -> StringSupplier.singleCharStringSupplier(c).supplyString(length);
        }
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
        return makeTestData(
                templatesMinCount,
                templatesMaxCount,
                minTemplateLength,
                maxTemplateLength,
                minStreamLength,
                maxStreamLength,
                StringSupplier.randomStringSupplier(),
                StringSupplier.randomStringSupplier());
    }

    /**
     * Generates a random test according to the given parameters for two-dimensional matrix matchers.<br/>
     * Rows are generated as random lines using {@link phoenix.templatesearcher.support
     * .Utility#randomString(int)}.
     * @param minTemplateWidth
     *         minimal width of template matrix.
     * @param maxTemplateWidth
     *         maximal width of template matrix.
     * @param minTemplateHeight
     *         minimal height of template matrix.
     * @param maxTemplateHeight
     *         maximal height of template matrix.
     * @param minSearchableWidth
     *         minimal width of matrix in which occurrences of template matrix are searched.
     * @param maxSearchableWidth
     *         maximal width of matrix in which occurrences of template matrix are searched.
     * @param minSearchableHeight
     *         minimal width of matrix in which occurrences of template matrix are searched.
     * @param maxSearchableHeight
     *         maximal height of matrix in which occurrences of template matrix are searched.
     * @return Pair (template matrix, searchable matrix).
     */
    public static ReadOnlyPair<IMatrix2D, IMatrix2D> makeTestData(int minTemplateWidth,
                                                                  int maxTemplateWidth,
                                                                  int minTemplateHeight,
                                                                  int maxTemplateHeight,
                                                                  int minSearchableWidth,
                                                                  int maxSearchableWidth,
                                                                  int minSearchableHeight,
                                                                  int maxSearchableHeight) {
        return makeTestData(
                minTemplateWidth,
                maxTemplateWidth,
                minTemplateHeight,
                maxTemplateHeight,
                minSearchableWidth,
                maxSearchableWidth,
                minSearchableHeight,
                maxSearchableHeight,
                RowSupplier.randomRowSupplier(),
                RowSupplier.randomRowSupplier());
    }

    /**
     * Generates a random test according to the given parameters for two-dimensional matrix matchers.
     * @param minTemplateWidth
     *         minimal width of template matrix.
     * @param maxTemplateWidth
     *         maximal width of template matrix.
     * @param minTemplateHeight
     *         minimal height of template matrix.
     * @param maxTemplateHeight
     *         maximal height of template matrix.
     * @param minSearchableWidth
     *         minimal width of matrix in which occurrences of template matrix are searched.
     * @param maxSearchableWidth
     *         maximal width of matrix in which occurrences of template matrix are searched.
     * @param minSearchableHeight
     *         minimal width of matrix in which occurrences of template matrix are searched.
     * @param maxSearchableHeight
     *         maximal height of matrix in which occurrences of template matrix are searched.
     * @param templateRowSupplier
     *         generator of string rows for the template matrix.
     * @param searchableRowSupplier
     *         generator of string rows for the searchable matrix.
     * @return Pair (template matrix, searchable matrix).
     */
    public static ReadOnlyPair<IMatrix2D, IMatrix2D> makeTestData(int minTemplateWidth,
                                                                  int maxTemplateWidth,
                                                                  int minTemplateHeight,
                                                                  int maxTemplateHeight,
                                                                  int minSearchableWidth,
                                                                  int maxSearchableWidth,
                                                                  int minSearchableHeight,
                                                                  int maxSearchableHeight,
                                                                  RowSupplier templateRowSupplier,
                                                                  RowSupplier searchableRowSupplier) {
        int templateWidth = randomInt(minTemplateWidth, maxTemplateWidth);
        int templateHeight = randomInt(minTemplateHeight, maxTemplateHeight);
        int searchableWidth = randomInt(minSearchableWidth, maxSearchableWidth);
        int searchableHeight = randomInt(minSearchableHeight, maxSearchableHeight);

        String[] template = new String[templateHeight];
        for (int i = 0; i < template.length; i++) {
            template[i] = templateRowSupplier.supplyRow(i, templateWidth);
        }

        String[] searchable = new String[searchableHeight];
        for (int i = 0; i < searchable.length; i++) {
            searchable[i] = searchableRowSupplier.supplyRow(i, searchableWidth);
        }

        return new ReadOnlyPair<>(new Matrix2D(template), new Matrix2D(searchable));
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
     * @param templateSupplier
     *         generator of strings for templates.
     * @param streamSupplier
     *         generator of stream string.
     * @return a pair (stream, templates[])
     */
    public static ReadOnlyPair<String, String[]> makeTestData(int templatesMinCount,
                                                              int templatesMaxCount,
                                                              int minTemplateLength,
                                                              int maxTemplateLength,
                                                              int minStreamLength,
                                                              int maxStreamLength,
                                                              StringSupplier streamSupplier,
                                                              StringSupplier templateSupplier) {
        String stream = streamSupplier.supplyString(randomInt(minStreamLength, maxStreamLength));
        int templatesCount = randomInt(templatesMinCount, templatesMaxCount);

        PineForest<FinalPineNode> templatesForest = new PineForest<>(new FinalPineNode());

        /*
        The problem is that randomString() method called many times can generate equal lines.
        If duplicate line is attempted to be added, it is regenerated some times, but not many,
        because it would take very long long time.
         */
        int repeatsWithSameI = 0;

        int maxRepeatsWithSameI = 10;

        for (int i = 0; i < templatesCount; i++) {
            String template = templateSupplier.supplyString(randomInt(minTemplateLength, maxTemplateLength));
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

    private static int indexOf(String source, String substr, int fromIndex, ICharComparator comparator) {
        for (int i = fromIndex, iLast = source.length() - substr.length(); i <= iLast; i++) {
            boolean matchFound = true;
            for (int srcInd = i, subInd = 0, subLen = substr.length(); subInd < subLen; srcInd++, subInd++) {
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
     * Finds occurrences of the template matrix in another matrix very slowly O(|A| * |B|) if |A| and |B|
     * are their sizes.
     * @return list of occurrences. Can be empty.
     */
    public static List<IOccurrence2D> matchMatrix2DVeryNaive(IMatrix2D template, IMatrix2D searchable) {
        List<IOccurrence2D> occurrences = new LinkedList<>();

        for (int x = 0; x <= searchable.getWidth() - template.getWidth(); x++) {
            for (int y = 0; y <= searchable.getHeight() - template.getHeight(); y++) {
                IMatrix2D streamSubMatrix =
                        searchable.subMatrix(x, y, template.getWidth(), template.getHeight());

                if (streamSubMatrix.equals(template)) {
                    occurrences.add(
                            new Occurrence2D(
                                    x + template.getWidth() - 1, y + template.getHeight() - 1, 0));
                }
            }
        }

        return occurrences;
    }

    /**
     * Finds matches in the stream using {@link String } methods.
     * @return list of occurrences. Can be empty.
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

    public static void addTestDataOneLongTemplateShortStream(Collection<Object[]> data, int testsCount) {
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
     * @param <T>
     *         type of occurrences. Must have implemented method {@link java.lang.Object#equals(Object)}} and
     *         {@link Object#hashCode()}.
     * @throws AssertionFailedError
     *         if result sets are not equal (order is ignored)
     */
    public static <T> void checkOccurrenceResults(List<T> actual, List<T> expected)
            throws AssertionFailedError {
        HashSet<T> checkSet = new HashSet<>();

        checkSet.addAll(actual);

        for (T occ : expected) {
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
     * Performs a single test with given parameters.<br/>
     * Equivalent to call {@code testMatchStream(matcher, data.getKey(), data.getValue(), comparator)}.
     * @param data
     *         test data containing stream and templates array.
     */
    public static void testMatchStream(IMetaTemplateMatcher matcher,
                                       ReadOnlyPair<String, String[]> data,
                                       ICharComparator comparator) {
        testMatchStream(matcher, data.getKey(), data.getValue(), comparator);
    }

    /**
     * Performs a single test on two-dimensional matrix with given parameters.<br/>
     * Equivalent to call {@code testMatchMatrix(matcher, testData.getKey(), testData.getValue())}.
     * @param matcher
     *         matrix matcher.
     * @param testData
     *         pair (template matrix, searchable matrix).
     */
    public static void testMatchMatrix(IMetaTemplate2DMatcher matcher,
                                       ReadOnlyPair<IMatrix2D, IMatrix2D> testData) {
        testMatchMatrix(matcher, testData.getKey(), testData.getValue());
    }

    /**
     * Performs a single test on two-dimensional matrix matcher with given parameters.
     * @param matcher
     *         matrix matcher.
     * @param template
     *         template matrix.
     * @param searchable
     *         matrix to search in.
     */
    public static void testMatchMatrix(IMetaTemplate2DMatcher matcher,
                                       IMatrix2D template,
                                       IMatrix2D searchable) {
        List<IOccurrence2D> expectedResults = matchMatrix2DVeryNaive(template, searchable);
        matcher.addTemplate(template);
        List<IOccurrence2D> actualResults = matcher.matchMatrix(searchable);

        try {
            checkOccurrenceResults(actualResults, expectedResults);
        } catch (AssertionFailedError ass) {
            System.err.println("Failure: " + ass.getMessage());
            System.err.println("Searchable: " + searchable);
            System.err.println("Templates: " + template);
            throw ass;
        }
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
     *         If true, templates are added using {@link phoenix.templatesearcher.api
     *         .IMetaTemplateMatcher#addTemplate(String)}
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
     *         Stream to test the matcher against.
     * @param templates
     *         Templates to search in the stream.
     */
    public static void testMatchStream(IMetaTemplateMatcher matcher,
                                       String stream,
                                       String[] templates,
                                       ICharComparator comparator) {
        testMatchStream(matcher, stream, templates, comparator, true);
    }

    /**
     * Interface for string generators.
     */
    @FunctionalInterface
    public static interface StringSupplier {
        /**
         * Generates a string that must have {@code length} characters.
         * @param length
         *         number of characters in the string.
         */
        String supplyString(int length);

        /**
         * Generates string filled with the same character.
         */
        static StringSupplier singleCharStringSupplier(char c) {
            return (length) -> {
                char[] chars = new char[length];
                Arrays.fill(chars, c);
                return new String(chars);
            };
        }

        /**
         * @see phoenix.templatesearcher.support.Utility#randomString(int)
         */
        static StringSupplier randomStringSupplier() {
            return Utility::randomString;
        }
    }
}
