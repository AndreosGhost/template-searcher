package phoenix.templatesearcher.test.matchers;

import phoenix.templatesearcher.api.IMatrix2D;
import phoenix.templatesearcher.api.IMetaTemplate2DMatcher;
import phoenix.templatesearcher.support.ReadOnlyPair;

/**
 * Base interface for test classes whose tests can be reused via inheritance.
 */
public interface IMatcher2DTest {
    /**
     * Obtains a new instance of currently tested implementation of {@link
     * phoenix.templatesearcher.api.IMetaTemplate2DMatcher}.
     */
    IMetaTemplate2DMatcher obtainFreshMatcher();

    default void testMatchMatrix(IMatrix2D template, IMatrix2D searchable) {
        MatcherTester.testMatchMatrix(obtainFreshMatcher(), template, searchable);
    }

    default void testMatchMatrix(ReadOnlyPair<IMatrix2D, IMatrix2D> testData) {
        MatcherTester.testMatchMatrix(obtainFreshMatcher(), testData);
    }
}
