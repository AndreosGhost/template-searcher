package phoenix.templatesearcher.test;

import static phoenix.templatesearcher.support.Utility.randomInt;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import phoenix.templatesearcher.matchers.SingleTemplateMatcher;

@RunWith(Parameterized.class)
public class SingleTemplateMatcherTest extends BasicTest<SingleTemplateMatcher> {
    @Override
    protected SingleTemplateMatcher obtainFreshMatcher() {
	return new SingleTemplateMatcher();
    }

    /**
     * Generates parameters for multiple tests.
     * 
     * @return
     */
    @Parameters
    public static Collection<Object[]> data() {
	LinkedList<Object[]> tests = new LinkedList<>();

	int testsCount;

	// one long template, but short stream
	testsCount = randomInt(10, 1000);

	for (int test = 0; test < testsCount; test++) {
	    tests.add(BasicTest
		    .wrapParameters(makeTestData(1, 1, 15, 20, 1, 30)));
	}

	// one template tests
	testsCount = randomInt(10, 1000);

	for (int test = 0; test < testsCount; test++) {
	    tests.add(BasicTest
		    .wrapParameters(makeTestData(1, 1, 1, 20, 1, 100)));
	}

	return tests;
    }

    

    // method testMatchStream is inherited from BasicTest
}
