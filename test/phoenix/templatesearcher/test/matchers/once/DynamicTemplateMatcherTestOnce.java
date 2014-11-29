package phoenix.templatesearcher.test.matchers.once;

import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.matchers.DynamicTemplateMatcher;

public class DynamicTemplateMatcherTestOnce extends NaiveTemplateMatcherTestOnce {
    @Override
    public IMetaTemplateMatcher obtainFreshMatcher() {
        return new DynamicTemplateMatcher();
    }
}
