package phoenix.templatesearcher.api;

/**
 * Class for describing where one matrix can be found in another matrix.
 */
public interface IOccurrence2D {
    /**
     * Returns x-index of right bottom point of template matrix occurrence (inclusive).
     * @return
     */
    int getX();

    /**
     * Returns y-index of right bottom point of template matrix occurrence (inclusive).
     * @return
     */
    int getY();

    /**
     * Returns template id which occurrence is described.
     * @return
     */
    int getTemplateID();
}
