package phoenix.templatesearcher.test.matchers;

import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.support.ICharComparator;

/**
 * Base interface for test classes whose tests can be reused via inheritance.
 */
public interface IMatcherTest {
    /**
     * Obtains a new instance of currently tested implementation of {@link
     * phoenix.templatesearcher.api.IMetaTemplateMatcher}.
     * @return
     */
    IMetaTemplateMatcher obtainFreshMatcher();

    /**
     * Obtains char comparator used to distinguish characters.<br/>
     * In most cases you will need {@link phoenix.templatesearcher.support
     * .ICharComparator#DEFAULT_COMPARATOR}.<br/>
     * Note that your matcher must use the same {@link phoenix.templatesearcher.support.ICharComparator}
     * that is returned by this method.
     * @return
     */
    ICharComparator obtainCharComparator();
}
