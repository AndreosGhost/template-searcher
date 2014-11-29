package phoenix.templatesearcher.test.matchers.highload;

import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.matchers.StaticTemplateMatcher;

public class StaticTemplateMatcherTestHighLoad extends NaiveTemplateMatcherTestHighLoad {
    @Override
    public IMetaTemplateMatcher obtainFreshMatcher() {
        return new StaticTemplateMatcher();
    }
}
