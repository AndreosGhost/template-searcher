package phoenix.templatesearcher.highloadtests;

import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.matchers.DynamicTemplateMatcher;

public class DynamicTemplateMatcherTestHighLoad extends NaiveTemplateMatcherTestHighLoad {
    @Override
    public IMetaTemplateMatcher obtainFreshMatcher() {
        return new DynamicTemplateMatcher();
    }
}
