package phoenix.templatesearcher.test.once;

import static phoenix.templatesearcher.support.Utility.*;

import org.junit.Assert;
import org.junit.Test;

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
}
