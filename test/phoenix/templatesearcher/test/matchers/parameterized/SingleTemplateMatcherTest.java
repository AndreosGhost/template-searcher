package phoenix.templatesearcher.test.matchers.parameterized;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.matchers.SingleTemplateMatcher;
import phoenix.templatesearcher.support.ICharComparator;

import java.util.Collection;
import java.util.LinkedList;

import static phoenix.templatesearcher.support.Utility.*;
import static phoenix.templatesearcher.test.matchers.MatcherTester.*;

@RunWith(Parameterized.class)
public class SingleTemplateMatcherTest extends BasicMatcherTest {
    /**
     * Generates parameters for multiple tests.
     * @return
     */
    @Parameters
    public static Collection<Object[]> data() {
        LinkedList<Object[]> tests = new LinkedList<>();

        addTestDataOneLongTemplateShortStream(tests, randomInt(10, 50));
        addTestDataOneTemplate(tests, randomInt(30, 1000));

        return tests;
    }

    @Override
    public IMetaTemplateMatcher obtainFreshMatcher() {
        return new SingleTemplateMatcher();
    }

    @Override
    public ICharComparator obtainCharComparator() {
        return ICharComparator.DEFAULT_COMPARATOR;
    }

//    @Override
//    @Test
//    public void testMatchStream() {
//        //perform test
//        super.testMatchStream();
//    }

}
