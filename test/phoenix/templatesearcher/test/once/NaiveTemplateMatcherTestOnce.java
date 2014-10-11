package phoenix.templatesearcher.test.once;

import static phoenix.templatesearcher.support.Utility.*;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import phoenix.templatesearcher.matchers.NaiveTemplateMatcher;

public class NaiveTemplateMatcherTestOnce {
    @Test
    public void testAddTemplates() {
	NaiveTemplateMatcher matcher = new NaiveTemplateMatcher();

	int templatesCount = randomNonZeroCount(20);

	for (int i = 0; i < templatesCount; i++) {
	    int templateID = matcher
		    .addTemplate(randomString(randomNonZeroCount(100)));
	    Assert.assertEquals("Illegal template id given", i, templateID);
	}
    }
    
    @Rule
    public ExpectedException exception;
    
    @Test
    public void testAddEmptyTemplate() {
	exception.expect(IllegalArgumentException.class);
	exception.expectMessage("Template must not be empty");
	
	NaiveTemplateMatcher matcher = new NaiveTemplateMatcher();
	
	matcher.addTemplate("");
    }
    
    @Test
    public void testAddDuplicateTemplate() {
	exception.expect(IllegalArgumentException.class);

	NaiveTemplateMatcher matcher = new NaiveTemplateMatcher();
	
	String duplicate = randomString(10);
	
	exception.expectMessage("Duplicate template given: " + duplicate);
	
	matcher.addTemplate(duplicate);
	matcher.addTemplate(duplicate);
    }
}
