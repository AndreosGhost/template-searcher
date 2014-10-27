package phoenix.templatesearcher.test.matchers.parameterized;

import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.matchers.StaticTemplateMatcher;

public class StaticTemplateMatcherTest extends NaiveTemplateMatcherTest {
    /*
    All tests that are suitable for NaiveTemplateMatcher are also suitable for StaticTemplateMatcher.
     */

    @Override
    public IMetaTemplateMatcher obtainFreshMatcher() {
        return new StaticTemplateMatcher();
    }
}
