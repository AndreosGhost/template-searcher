package phoenix.templatesearcher.test.matchers.once;

import org.junit.Ignore;
import org.junit.Test;
import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.matchers.WildcardSingleTemplateMatcher;
import phoenix.templatesearcher.algo.api.ICharComparator;

public class WildcardSingleTemplateMatcherTestOnce extends SingleTemplateMatcherTestOnce {
    @Override
    public IMetaTemplateMatcher obtainFreshMatcher() {
        return new WildcardSingleTemplateMatcher();
    }

    @Deprecated
    @Ignore
    @Override
    public void testAppendCharBeforeAddingTemplate() {
        // Behaviour not supported.
    }

    @Deprecated
    @Ignore
    @Override
    public void testPrependCharBeforeAddingTemplate() {
        // Behaviour not supported.
    }

    @Deprecated
    @Ignore
    @Override
    public void testAppendCharToTemplate() {
        // Behaviour not supported.
    }

    @Deprecated
    @Ignore
    @Override
    public void testPrependCharToTemplate() {
        // Behaviour not supported.
    }

    @Override
    public ICharComparator obtainCharComparator() {
        return ICharComparator.WILDCARD_COMPARATOR;
    }

    @Test
    public void testOneWildcardAtTheBeginning() {
        String stream = "Babbcabbaccabbacabacbabcbbabc";
        String template = "?bba";

        testMatchSingleTemplate(stream, template);
    }

    @Test
    public void testOneWildcardAtTheEnd() {
        String stream = "AcljclsdccldcCabcacbdcaCdddac";
        String template = "ac?";

        testMatchSingleTemplate(stream, template);
    }

    @Test
    public void testOneWildcardAtTheMiddle() {
        String stream = "AbccabAbabdDAcdabadcdbadabADBADbdacadaad";
        String template = "d?A";

        testMatchSingleTemplate(stream, template);
    }

    @Test
    public void testSeveralWildCards() {
        String stream = "Able_Enable_Disable_ability_unable_improvable_NoTAbLe";
        String template = "??ble?";

        testMatchSingleTemplate(stream, template);
    }

    @Test
    public void testWildCardsBeginAndMiddle() {
        String stream = "abbcabababccabacabbacababacacbaacabacabac";
        String template = "?a?b";

        testMatchSingleTemplate(stream, template);
    }

    @Test
    public void testWildCardsOnlyMiddle() {
        String stream = "abacbaccabacaccbacbabcccbcabababcacaabcabcba";
        String template = "a??b";

        testMatchSingleTemplate(stream, template);
    }

    @Test
    public void testWildCardsBeginMiddleEnd() {
        String stream = "accbbcbabcabcbacccaababcabbcbaacababbcacbacaacbab";
        String template = "?a?b?";

        testMatchSingleTemplate(stream, template);
    }

    @Test
    public void testWildCardsMiddleEnd() {
        String stream = "abcbcacbacbabacabcbacbabbccabaacbbabccacbacbabca";
        String template = "a?b?";

        testMatchSingleTemplate(stream, template);
    }

    @Test
    public void testOnlyWildCards() {
        String stream = "No matter what stream it is";
        String template = "????";

        testMatchSingleTemplate(stream, template);
    }

    @Test
    public void testLongOnlyWildCardsShortStream() {
        String stream = "short stream";
        String template = "?????????????????????";

        testMatchSingleTemplate(stream, template);
    }
}
