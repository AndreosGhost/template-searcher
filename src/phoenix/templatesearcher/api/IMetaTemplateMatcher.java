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
     * @return unique template ID
     */
    public int addTemplate(String template)
            throws UnsupportedOperationException, IllegalArgumentException;

    /**
     * Finds template matches in character stream
     * @param stream
     * @return list of occurences; each occurence is pair (index of last template symbol, template
     * ID);
     */
    public List<IOccurrence> matchStream(ICharStream stream);
}
