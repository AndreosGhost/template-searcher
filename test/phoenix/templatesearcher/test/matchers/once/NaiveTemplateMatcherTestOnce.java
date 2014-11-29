package phoenix.templatesearcher.test.matchers.once;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.exception.DuplicateLineException;
import phoenix.templatesearcher.matchers.NaiveTemplateMatcher;
import phoenix.templatesearcher.algo.api.ICharComparator;
import phoenix.templatesearcher.test.matchers.IMatcherTest;
import phoenix.templatesearcher.test.matchers.MatcherTester;

import static phoenix.templatesearcher.support.Utility.*;

@RunWith(JUnit4.class)
public class NaiveTemplateMatcherTestOnce implements IMatcherTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    protected IMetaTemplateMatcher matcher;

    @Before
    public void prepare() {
        matcher = obtainFreshMatcher();
    }

    @After
    public void cleanup() {
        matcher = null;
    }

    @Test
    public void testAddTemplates() {
        int templatesCount = randomNonZeroCount(20);

        for (int i = 0; i < templatesCount; i++) {
            try {
                int templateID = matcher.addTemplate(randomString(randomNonZeroCount(100)));
                Assert.assertEquals("Illegal template id given", i, templateID);
            } catch (DuplicateLineException exc) {
                // Ignore it.
            }
        }
    }

    @Test
    public void testAddNullTemplate() {
        exception.expect(NullPointerException.class);
        exception.expectMessage("Template must not be null");

        matcher.addTemplate(null);
    }

    @Test
    public void testAddEmptyTemplate() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Template must not be empty");

        matcher.addTemplate("");
    }

    @Test
    public void testAddDuplicateTemplate() {
        String duplicate = randomString(10);

        exception.expect(DuplicateLineException.class);
        exception.expectMessage("Line has been already added: " + duplicate);

        matcher.addTemplate(duplicate);
        matcher.addTemplate(duplicate);
    }

    @Test
    public void testSearchSimpleOccurrence() {
        MatcherTester.testMatchStream(
                matcher, "Templ1Temp2l", new String[] {"Templ"}, obtainCharComparator());
    }

    @Test
    public void testSearchCrossingOccurrences() {
        MatcherTester.testMatchStream(
                matcher, "dabce", new String[] {"abc", "bc", "bce"}, obtainCharComparator());
    }

    @Override
    public IMetaTemplateMatcher obtainFreshMatcher() {
        return new NaiveTemplateMatcher();
    }

    @Override
    public ICharComparator obtainCharComparator() {
        return ICharComparator.DEFAULT_COMPARATOR;
    }
}
