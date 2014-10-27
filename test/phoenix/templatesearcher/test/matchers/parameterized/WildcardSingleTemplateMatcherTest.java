package phoenix.templatesearcher.test.matchers.parameterized;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.matchers.WildcardSingleTemplateMatcher;
import phoenix.templatesearcher.support.ICharComparator;

import static phoenix.templatesearcher.support.Utility.*;

@RunWith(Parameterized.class)
public class WildcardSingleTemplateMatcherTest extends SingleTemplateMatcherTest {
    @Override
    public ICharComparator obtainCharComparator() {
        return ICharComparator.WILDCARD_COMPARATOR;
    }

    @Override
    public IMetaTemplateMatcher obtainFreshMatcher() {
        return new WildcardSingleTemplateMatcher();
    }

    @Test
    @Override
    public void testMatchStream() {
        //Making an injection: inserting wildcard symbols to random places
        boolean doInjection = randomNonZeroCount(100) <= 50;

        String template = templates[0];

        if (doInjection) {
            int insertsCount = randomInt(1, template.length());

            for (int i = 0; i < insertsCount; i++) {
                int pos = randomInt(0, template.length());
                template = template.substring(0, pos) + ICharComparator.WILDCARD_SYMBOL + template
                        .substring(pos);
            }
        }

        super.testMatchStream();
    }
}
