package phoenix.templatesearcher.api;

import java.util.List;

/**
 * Interface for searching matrices inside each other.
 */
public interface IMetaTemplate2DMatcher {
    /**
     * Adds template to the list of templates
     * @param template
     * @return Unique template ID.
     * @throws UnsupportedOperationException
     *         If adding template is forbidden.
     * @throws IllegalArgumentException
     *         If this template has been already added or template is null/empty.
     */
    int addTemplate(IMatrix2D template) throws UnsupportedOperationException, IllegalArgumentException;

    /**
     * Finds template matches in matrix of characters.
     * @param matrix
     * @return list of occurrences; each occurrence contains template id and position of occurrence.
     */
    List<IOccurrence2D> matchMatrix(IMatrix2D matrix);
}
