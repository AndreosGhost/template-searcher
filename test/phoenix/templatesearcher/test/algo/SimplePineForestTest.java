package phoenix.templatesearcher.test.algo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import phoenix.templatesearcher.algo.FinalPineNode;
import phoenix.templatesearcher.algo.PineForest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static phoenix.templatesearcher.support.Utility.*;

/**
 * Test for adding unique lines and pulling them.
 */
@RunWith(Parameterized.class)
public class SimplePineForestTest {
    @Parameter
    public String[] strings;

    @Parameters
    public static Iterable<Object[]> data() {
        List<Object[]> data = new LinkedList<>();

        int testsCount = randomInt(100, 500);

        for (int test = 0; test < testsCount; test++) {
            int stringsCount = randomInt(5, test + 10);

            String[] strings = new String[stringsCount];

            for (int str = 0; str < stringsCount; str++) {
                // Strings can be duplicate. Not checking it here.
                strings[str] = randomString(randomInt(1, 25));
            }

            data.add(new Object[] {strings});
        }

        return data;
    }

    @Test
    public void test() {
        try {
            Map<String, Integer> identitiesMap = new HashMap<>(strings.length * 4);

            PineForest<FinalPineNode> forest = new PineForest<>(new FinalPineNode());

            for (int str = 0, count = strings.length, idCounter = 0; str < count; str++) {
                String line = strings[str];
                Integer id = identitiesMap.get(line);
                if (id == null) {
                    id = idCounter++;
                    identitiesMap.put(line, id);
                    forest.addLine(line.toCharArray(), id);
                }
            }

            String[] forestLines = forest.listLines();

            Assert.assertEquals(
                    "Forest and naive lines count do not match", identitiesMap.size(), forestLines.length);
            for (int id = 0, count = forestLines.length; id < count; id++) {
                Integer expectedID = identitiesMap.get(forestLines[id]);

                Assert.assertNotNull(
                        "Forest line not found in naive container: " + forestLines[id], expectedID);
                Assert.assertEquals(
                        "Line forest ID and naive ID do not match", (int) expectedID, id);
            }
        } catch (Throwable t) {
            System.err.println("Test failure; data = " + Arrays.toString(strings));
            throw t;
        }
    }
}
