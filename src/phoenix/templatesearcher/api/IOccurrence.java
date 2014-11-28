package phoenix.templatesearcher.api;

public interface IOccurrence {
    /**
     * Returns right position of template occurrence.
     * @return
     */
    int getIndex();

    /**
     * Returns template id which occurrence is described.
     * @return
     */
    int getTemplateID();
}