package phoenix.templatesearcher.test.matchers.highload;

import org.junit.Test;
import phoenix.templatesearcher.api.IMetaTemplate2DMatcher;
import phoenix.templatesearcher.matchers.SingleTemplate2DMatcher;
import phoenix.templatesearcher.test.matchers.IMatcher2DTest;

import static phoenix.templatesearcher.test.matchers.MatcherTester.*;

public class SingleTemplate2DMatcherTestHighLoad implements IMatcher2DTest {
    @Override
    public IMetaTemplate2DMatcher obtainFreshMatcher() {
        return new SingleTemplate2DMatcher();
    }

    @Test
    public void testTemplateAndSearchableOfOneChar() {
        int templateSize = 50;
        int searchableSize = 1000;

        testMatchMatrix(
                makeTestData(
                        templateSize,
                        templateSize,
                        templateSize,
                        templateSize,
                        searchableSize,
                        searchableSize,
                        searchableSize,
                        searchableSize,
                        RowSupplier.singleCharRowSupplier('a'),
                        RowSupplier.singleCharRowSupplier('a')));
    }
}
