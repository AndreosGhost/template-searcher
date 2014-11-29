package phoenix.templatesearcher.test.matchers.highload;

import org.junit.Ignore;
import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.matchers.SingleTemplateMatcher;

public class SingleTemplateMatcherTestHighLoad extends NaiveTemplateMatcherTestHighLoad {
    @Override
    public IMetaTemplateMatcher obtainFreshMatcher() {
        return new SingleTemplateMatcher();
    }

    @Override
    @Ignore
    @Deprecated
    public void testSeveralTemplatesWithEqualSymbolsAndStreamWithEqualSymbols() { }

    @Override
    @Ignore
    @Deprecated
    public void testSeveralTemplatesWithSeveralSymbolsAndStreamWithSeveralSymbols() { }
}
