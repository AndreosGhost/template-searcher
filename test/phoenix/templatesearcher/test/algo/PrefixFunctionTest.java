package phoenix.templatesearcher.test.algo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import phoenix.templatesearcher.algo.PrefixFunction;
import phoenix.templatesearcher.support.ICharComparator;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static phoenix.templatesearcher.support.Utility.*;

@RunWith(Parameterized.class)
public class PrefixFunctionTest {
    @Parameter
    public String testString;

    @Parameters
    public static Collection<Object[]> data() {
        List<Object[]> strings = new LinkedList<>();

        //adding random tests
        int testsCount = randomInt(100, 300);

        int minLength = 10;
        int maxLength = 100;

        for (int test = 0; test < testsCount; test++) {
            strings.add(new Object[] {randomString(randomInt(minLength, maxLength))});
        }

        return strings;
    }

    @Test
    public void test() {
        int[] piActual = new int[testString.length()];
        int[] piExpected = new int[testString.length()];

        PrefixFunction.countPrefixFunction(testString.toCharArray(), piActual);
        countPrefixFunctionNaive(testString.toCharArray(), piExpected);

        Assert.assertArrayEquals(
                "Prefix function is evaluated not correctly", piExpected, piActual);
    }

    protected void countPrefixFunctionNaive(char[] data, int[] pi) {
        countPrefixFunctionNaive(data, pi, 0, data.length, ICharComparator.DEFAULT_COMPARATOR);
    }

    protected void countPrefixFunctionNaive(char[] data,
                                            int[] pi,
                                            int begin,
                                            int end,
                                            ICharComparator comparator) {
        if (begin == 0) {
            pi[0] = 0;
            begin++;
        }
        for (int j = begin; j < end; j++) {
            pi[j] = 0;
            for (int pfunc = pi[j - 1] + 1; pfunc > 0; pfunc--) {
                boolean doesEquals = true;

                for (int i = 0, k = j - pfunc + 1; i < pfunc; i++, k++) {
                    if (!comparator.areEqual(data[i], data[k])) {
                        doesEquals = false;
                        break;
                    }
                }

                if (doesEquals) {
                    pi[j] = pfunc;
                    break;
                }
            }
        }
    }
}
