package phoenix.templatesearcher.test;

import static phoenix.templatesearcher.support.Utility.randomInt;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import phoenix.templatesearcher.matchers.NaiveTemplateMatcher;

@RunWith(Parameterized.class)
public class NaiveTemplateMatcherTest extends BasicTest<NaiveTemplateMatcher> {
    /**
     * Generates parameters for multiple tests.
     * 
     * @return
     */
    @Parameters
    public static Collection<Object[]> data() {
	LinkedList<Object[]> tests = new LinkedList<>();

	int testsCount;

	// one template tests
	testsCount = randomInt(10, 1000);

	for (int test = 0; test < testsCount; test++) {
	    tests.add(BasicTest
		    .wrapParameters(makeTestData(1, 1, 1, 5, 10, 30)));
	}

	// one long template, but short stream
	testsCount = randomInt(10, 1000);

	for (int test = 0; test < testsCount; test++) {
	    tests.add(BasicTest
		    .wrapParameters(makeTestData(1, 1, 15, 20, 1, 30)));
	}

	// completely random tests
	testsCount = randomInt(10, 1000);

	for (int test = 0; test < testsCount; test++) {
	    tests.add(BasicTest.wrapParameters(makeTestData(1, 10, 1, 20, 1,
		    100)));
	}

	return tests;
    }

    @Override
    protected NaiveTemplateMatcher obtainFreshMatcher() {
	return new NaiveTemplateMatcher();
    }

}
