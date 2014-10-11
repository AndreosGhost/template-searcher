package phoenix.templatesearcher.test.once;

import static phoenix.templatesearcher.support.Utility.randomString;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import phoenix.templatesearcher.matchers.SingleTemplateMatcher;
import phoenix.templatesearcher.test.support.StringCharStream;

public class SingleTemplateMatcherTestOnce {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testAddTwoTemplates() {
	exception.expect(UnsupportedOperationException.class);
	exception.expectMessage("Cannot add more then one template");

	SingleTemplateMatcher matcher = new SingleTemplateMatcher();

	matcher.addTemplate(randomString(10));
	matcher.addTemplate(randomString(10));
    }

    @Test
    public void testAddTemplatesBetweenProcessing() {
	exception.expect(UnsupportedOperationException.class);
	exception.expectMessage("Cannot add more then one template");

	SingleTemplateMatcher matcher = new SingleTemplateMatcher();

	matcher.addTemplate(randomString(10));
	matcher.matchStream(new StringCharStream(randomString(100)));
	matcher.addTemplate(randomString(10));
    }
}
