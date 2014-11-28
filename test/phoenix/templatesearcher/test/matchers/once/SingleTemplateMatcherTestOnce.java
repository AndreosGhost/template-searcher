package phoenix.templatesearcher.test.matchers.once;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.matchers.SingleTemplateMatcher;
import phoenix.templatesearcher.test.matchers.MatcherTester;
import phoenix.templatesearcher.test.support.StringCharStream;

import static phoenix.templatesearcher.support.Utility.*;

@RunWith(JUnit4.class)
public class SingleTemplateMatcherTestOnce extends NaiveTemplateMatcherTestOnce {
    @Test
    public void testAddTwoTemplates() {
        exception.expect(UnsupportedOperationException.class);
        exception.expectMessage("Cannot add more then one template");

        matcher.addTemplate(randomString(10));
        matcher.addTemplate(randomString(10));
    }

    protected void testMatchSingleTemplate(String stream, String template) {
        MatcherTester.testMatchStream(matcher, stream, new String[] {template}, obtainCharComparator());
    }

    @Deprecated
    @Ignore
    @Override
    public void testAddTemplates() {
        // Multiple templates handling not supported by SingleTemplateMatcher.
    }

    @Deprecated
    @Ignore
    @Override
    public void testAddDuplicateTemplate() {
        // Multiple (event duplicate) templates handling not supported by SingleTemplateMatcher.
    }

    @Deprecated
    @Ignore
    @Override
    public void testSearchCrossingOccurrences() {
        // Do nothing.
    }

    @Override
    public IMetaTemplateMatcher obtainFreshMatcher() {
        return new SingleTemplateMatcher();
    }

    @Test
    public void testAppendCharBeforeAddingTemplate() {
        exception.expect(UnsupportedOperationException.class);
        exception.expectMessage("Template not set yet");

        ((SingleTemplateMatcher) matcher).appendChar('c');
    }

    @Test
    public void testPrependCharBeforeAddingTemplate() {
        exception.expect(UnsupportedOperationException.class);
        exception.expectMessage("Template not set yet");

        ((SingleTemplateMatcher) matcher).prependChar('c');
    }

    @Test
    public void testAppendCharToTemplate() {
        String stream = "aabcbabccabbabbccd";
        String template = "abc";
        char appendedChar = 'c';

        MatcherTester.testMatchStream(matcher, stream, new String[] {template}, obtainCharComparator());
        ((SingleTemplateMatcher) matcher).appendChar(appendedChar);
        MatcherTester.testMatchStream(
                matcher, stream, new String[] {template + appendedChar}, obtainCharComparator(), false);
    }

    @Test
    public void testPrependCharToTemplate() {
        String stream = "aabcbabcabcacccbbacabbabbccd";
        String template = "abc";
        char prependedChar = 'a';

        MatcherTester.testMatchStream(matcher, stream, new String[] {template}, obtainCharComparator());
        ((SingleTemplateMatcher) matcher).prependChar(prependedChar);
        MatcherTester.testMatchStream(
                matcher, stream, new String[] {prependedChar + template}, obtainCharComparator(), false);
    }

    @Test
    public void testAddTemplatesBetweenProcessing() {
        exception.expect(UnsupportedOperationException.class);
        exception.expectMessage("Cannot add more then one template");

        matcher.addTemplate(randomString(10));
        matcher.matchStream(new StringCharStream(randomString(100)));
        matcher.addTemplate(randomString(10));
    }
}
