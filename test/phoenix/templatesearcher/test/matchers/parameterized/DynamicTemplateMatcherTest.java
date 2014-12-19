package phoenix.templatesearcher.test.matchers.parameterized;

import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.exception.DuplicateLineException;
import phoenix.templatesearcher.matchers.DynamicTemplateMatcher;
import phoenix.templatesearcher.support.Utility;
import phoenix.templatesearcher.test.matchers.MatcherTester;

import java.util.LinkedList;
import java.util.List;

public class DynamicTemplateMatcherTest extends NaiveTemplateMatcherTest {
    private static DynamicTemplateMatcher matcher;

    private static List<String> templatesList;

    private static int counter = 0;

    @Override
    public IMetaTemplateMatcher obtainFreshMatcher() {
        counter--;
        if (counter <= 0) {
            counter = Utility.randomInt(5, 20);
            matcher = new DynamicTemplateMatcher();
            templatesList = new LinkedList<>();
        }
        return matcher;
    }

    @Override
    public void testMatchStream() {
        IMetaTemplateMatcher matcher = obtainFreshMatcher();

        for (String s : templates) {
            boolean added = false;

            try {
                matcher.addTemplate(s);
                added = true;
            } catch (DuplicateLineException exc) {

            }
            if (added) {
                templatesList.add(s);
            }
        }

        MatcherTester.testMatchStream(
                matcher,
                stream,
                templatesList.toArray(new String[templatesList.size()]),
                obtainCharComparator(),
                false);
    }
}
