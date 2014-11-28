package phoenix.templatesearcher.exception;

/**
 * This exceptions occurs when user attempts to add the same line to the same pine forest twice.
 */
public class DuplicateLineException extends IllegalArgumentException {
    public DuplicateLineException(String line) {
        super("Line has been already added: " + line);
    }
}
