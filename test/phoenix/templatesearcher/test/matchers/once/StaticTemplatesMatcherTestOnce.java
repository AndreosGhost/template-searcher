package phoenix.templatesearcher.test.matchers.once;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.matchers.StaticTemplateMatcher;
import phoenix.templatesearcher.test.support.StringCharStream;

import static phoenix.templatesearcher.support.Utility.*;

@RunWith(JUnit4.class)
public class StaticTemplatesMatcherTestOnce extends NaiveTemplateMatcherTestOnce {

    @Override
    public IMetaTemplateMatcher obtainFreshMatcher() {
        return new StaticTemplateMatcher();
    }

    @Test
    public void testAddTemplatesBetweenProcessing() {
        exception.expect(UnsupportedOperationException.class);
        exception.expectMessage(
                "Cannot add templates after a first call to matchStream(ICharStream) is done");

        matcher.addTemplate(randomString(10));
        matcher.matchStream(new StringCharStream(randomString(100)));
        matcher.addTemplate(randomString(10));
    }
}
