package phoenix.templatesearcher.test.matchers.parameterized;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.matchers.NaiveTemplateMatcher;
import phoenix.templatesearcher.algo.api.ICharComparator;

import java.util.Collection;
import java.util.LinkedList;

import static phoenix.templatesearcher.support.Utility.*;
import static phoenix.templatesearcher.test.matchers.MatcherTester.*;

@RunWith(Parameterized.class)
public class NaiveTemplateMatcherTest extends BasicMatcherTest {
    /**
     * Generates parameters for multiple tests.
     */
    @Parameters
    public static Collection<Object[]> data() {
        LinkedList<Object[]> tests = new LinkedList<>();

        addTestDataOneLongTemplateShortStream(tests, randomInt(10, 50));
        addTestDataOneTemplate(tests, randomInt(100, 200));
        addTestDataMultipleTemplates(tests, randomInt(100, 500));
        addTestDataManyRepeats(tests, randomInt(100, 500));

        return tests;
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
