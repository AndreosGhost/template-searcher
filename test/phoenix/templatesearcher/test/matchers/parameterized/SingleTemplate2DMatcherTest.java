package phoenix.templatesearcher.test.matchers.parameterized;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import phoenix.templatesearcher.api.IMatrix2D;
import phoenix.templatesearcher.api.IMetaTemplate2DMatcher;
import phoenix.templatesearcher.matchers.SingleTemplate2DMatcher;
import phoenix.templatesearcher.support.ReadOnlyPair;
import phoenix.templatesearcher.test.matchers.IMatcher2DTest;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static phoenix.templatesearcher.support.Utility.*;
import static phoenix.templatesearcher.test.matchers.MatcherTester.*;

@RunWith(Parameterized.class)
public class SingleTemplate2DMatcherTest implements IMatcher2DTest {

    @Parameters
    public static Collection<Object[]> data() {
        int testsCount = randomInt(300, 2000);

        List<Object[]> tests = new LinkedList<>();

        for (int test = 0; test < testsCount; test++) {
            int templateMaxSize = test / 10 + 5;
            int searchableMinSize = templateMaxSize + test / 10;
            int searchableMaxSize = Math.max(test, searchableMinSize);

            ReadOnlyPair<IMatrix2D, IMatrix2D> testData = makeTestData(
                    1,
                    templateMaxSize,
                    1,
                    templateMaxSize,
                    searchableMinSize,
                    searchableMaxSize,
                    searchableMinSize,
                    searchableMaxSize);
            tests.add(new Object[] {testData.getKey(), testData.getValue()});
        }

        return tests;
    }

    @Parameter(value = 0)
    public IMatrix2D templateMatrix;

    @Parameter(value = 1)
    public IMatrix2D searchableMatrix;

    @Override
    public IMetaTemplate2DMatcher obtainFreshMatcher() {
        return new SingleTemplate2DMatcher();
    }

    @Test
    public void test() {
        testMatchMatrix(templateMatrix, searchableMatrix);
    }
}
