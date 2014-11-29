package phoenix.templatesearcher.exception;

/**
 * This exceptions occurs when user attempts to add the same line to the same pine forest twice.
 */
public class DuplicateLineException extends IllegalArgumentException {
    private final int assignedID;

    public DuplicateLineException(String line, int assignedID) {
        super("Line has been already added: " + line);
        this.assignedID = assignedID;
    }

    /**
     * Returns id assigned to the same line added before.
     * @return
     */
    public int getAssignedID() {
        return assignedID;
    }
}
