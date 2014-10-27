package phoenix.templatesearcher.test.matchers.parameterized;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import phoenix.templatesearcher.test.matchers.IMatcherTest;
import phoenix.templatesearcher.test.matchers.MatcherTester;

/**
 * This is a basic class for all tests on implementations of {@link phoenix.templatesearcher.api.IMetaTemplateMatcher}.
 * Each
 * extension of this class should provide the following method:<br/> {@code public static
 * Collection<Object[]> data() } as described in {@link Parameterized} runner.
 * @author phoenix
 * @see Parameterized
 */
@Ignore
@RunWith(Parameterized.class)
public abstract class BasicMatcherTest implements IMatcherTest {
    @Parameter(value = 0)
    public String stream;
    @Parameter(value = 1)
    public String[] templates;

    /**
     * This method is invoked by test runner and calls {@code testMatchStream(stream, templates) }.
     */
    @Test
    public void testMatchStream() {
        MatcherTester
                .testMatchStream(obtainFreshMatcher(), stream, templates, obtainCharComparator());
    }
}
