package phoenix.templatesearcher.highloadtests;

import org.junit.Test;
import phoenix.templatesearcher.algo.api.ICharComparator;
import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.matchers.WildcardSingleTemplateMatcher;
import phoenix.templatesearcher.support.ReadOnlyPair;
import phoenix.templatesearcher.test.matchers.MatcherTester;
import phoenix.templatesearcher.test.matchers.MatcherTester.StringSupplier;

import java.util.Arrays;
import java.util.Random;

import static phoenix.templatesearcher.support.Utility.*;

public class WildcardSingleTemplateMatcherTestHighLoad extends SingleTemplateMatcherTestHighLoad {
    @Override
    public IMetaTemplateMatcher obtainFreshMatcher() {
        return new WildcardSingleTemplateMatcher();
    }

    @Override
    public ICharComparator obtainCharComparator() {
        return ICharComparator.WILDCARD_COMPARATOR;
    }

    @Test
    public void testTemplateFullOfWildcards() {
        StringSupplier streamSupplier =
                StringSupplier.singleCharStringSupplier(ALPHABET[randomInt(0, ALPHABET.length - 1)]);
        StringSupplier templateSupplier = StringSupplier.singleCharStringSupplier(
                ICharComparator.WILDCARD_SYMBOL);
        ReadOnlyPair<String, String[]> data = MatcherTester
                .makeTestData(1, 1, 10000, 10000, 100000, 100000, streamSupplier, templateSupplier);
        performTest(data);
    }

    @Test
    public void testTemplateWCWCWStreamOneSymbol() {
        StringSupplier streamSupplier =
                StringSupplier.singleCharStringSupplier(ALPHABET[randomInt(0, ALPHABET.length - 1)]);
        StringSupplier wildCardSupplier = StringSupplier.singleCharStringSupplier(
                ICharComparator.WILDCARD_SYMBOL);
        StringSupplier templateSupplier = (size) -> {
            int wildPartSize = size / 20;
            int wordPartSize = size - 3 * wildPartSize;

            StringBuilder sb = new StringBuilder();
            sb.append(wildCardSupplier.supplyString(wildPartSize))
              .append(streamSupplier.supplyString(wordPartSize))
              .append(wildCardSupplier.supplyString(wildPartSize))
              .append(streamSupplier.supplyString(wordPartSize))
              .append(wildCardSupplier.supplyString(wildPartSize));

            return sb.toString();
        };

        ReadOnlyPair<String, String[]> data = MatcherTester
                .makeTestData(1, 1, 10000, 10000, 100000, 100000, streamSupplier, templateSupplier);
        performTest(data);
    }

    @Test
    public void testTemplateHalfWildCardsStreamOneSymbol() {
        StringSupplier streamSupplier =
                StringSupplier.singleCharStringSupplier(ALPHABET[randomInt(0, ALPHABET.length - 1)]);
        StringSupplier templateSupplier = (size) -> {
            char c = streamSupplier.supplyString(1).charAt(0);
            char[] data = new char[size];
            Arrays.fill(data, 0, size / 2, ICharComparator.WILDCARD_SYMBOL);
            Arrays.fill(data, size / 2, data.length - 1, c);

            Random random = new Random(size * LOCAL_RANDOM_BASE);

            for (int i = 0; i < data.length; i++) {
                int j = random.nextInt(data.length - i) + i;
                char swap = data[i];
                data[i] = data[j];
                data[j] = swap;
            }

            return new String(data);
        };

        ReadOnlyPair<String, String[]> data =
                MatcherTester.makeTestData(1, 1, 1000, 1000, 10000, 10000, streamSupplier, templateSupplier);
        performTest(data);
    }
}
