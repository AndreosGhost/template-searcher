package phoenix.templatesearcher.api;

import java.util.List;

/**
 * Interface for searching templates in text.
 * @author phoenix
 */
public interface IMetaTemplateMatcher {

    /**
     * Adds template to the list of templates
     * @param template
     * @return Unique template ID
     * @throws UnsupportedOperationException
     *         If adding template is forbidden.
     * @throws IllegalArgumentException
     *         If this template has been already added or template is null/empty.
     */
    int addTemplate(String template) throws UnsupportedOperationException, IllegalArgumentException;

    /**
     * Finds template matches in character stream
     * @param stream
     * @return list of occurrences; each occurrence is pair (index of last template symbol, template
     * ID);
     */
    List<IOccurrence> matchStream(ICharStream stream);
}
