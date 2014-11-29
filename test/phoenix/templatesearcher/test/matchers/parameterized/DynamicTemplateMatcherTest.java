package phoenix.templatesearcher.test.matchers.parameterized;

import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.matchers.DynamicTemplateMatcher;

public class DynamicTemplateMatcherTest extends NaiveTemplateMatcherTest {
    @Override
    public IMetaTemplateMatcher obtainFreshMatcher() {
        return new DynamicTemplateMatcher();
    }
}
