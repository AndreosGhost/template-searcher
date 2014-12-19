package phoenix.templatesearcher.test.matchers.parameterized;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import phoenix.templatesearcher.algo.api.ICharComparator;
import phoenix.templatesearcher.matchers.SingleTemplateMatcher;
import phoenix.templatesearcher.test.matchers.IMatcherTest;
import phoenix.templatesearcher.test.matchers.MatcherTester;

import java.util.Collection;
import java.util.LinkedList;

import static phoenix.templatesearcher.support.Utility.*;

@RunWith(Parameterized.class)
public final class SingleTemplateMatcherAppendPrependTest implements IMatcherTest {
    @Parameters
    public static Collection<Object[]> data() {
        Collection<Object[]> tests = new LinkedList<>();

        for (int i = 0; i < 100; i++) {
            String stream = randomString(10000);
            int appendProb = randomInt(MIN_PROB, MAX_PROB);
            int prependProb = randomInt(MIN_PROB, MAX_PROB);
            int attempts = randomInt(1, 10);
            tests.add(new Object[] {appendProb, prependProb, attempts, stream});
        }

        return tests;
    }

    @Parameter(value = 0)
    public int appendProb;

    @Parameter(value = 1)
    public int prependProb;

    @Parameter(value = 2)
    public int prependAppendAttempts;

    @Parameter(value = 3)
    public String stream;

    private static final int MIN_PROB = 1;
    private static final int MAX_PROB = 100;

    private static final SingleTemplateMatcher MATCHER = new SingleTemplateMatcher();
    private static final StringBuilder templateBuilder;

    static {
        templateBuilder = new StringBuilder(ALPHABET[randomInt(0, ALPHABET.length - 1)] + "");
        MATCHER.addTemplate(templateBuilder.toString());
    }

    @Override
    public SingleTemplateMatcher obtainFreshMatcher() {
        return MATCHER;
    }

    @Override
    public ICharComparator obtainCharComparator() {
        return ICharComparator.DEFAULT_COMPARATOR;
    }

    @Test
    public void testMatchStream() {
        for (int attempt = 0; attempt < prependAppendAttempts; attempt++) {
            if (randomInt(MIN_PROB, MAX_PROB) <= appendProb) {
                char c = ALPHABET[randomInt(0, ALPHABET.length - 1)];
                obtainFreshMatcher().appendChar(c);
                templateBuilder.insert(0, c);
            }
            if (randomInt(MIN_PROB, MAX_PROB) >= prependProb) {
                char c = ALPHABET[randomInt(0, ALPHABET.length - 1)];
                obtainFreshMatcher().prependChar(c);
                templateBuilder.insert(0, c);
            }
        }

        MatcherTester.testMatchStream(
                obtainFreshMatcher(),
                stream,
                new String[] {templateBuilder.toString()},
                obtainCharComparator(),
                false);
    }
}
