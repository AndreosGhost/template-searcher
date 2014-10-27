package phoenix.templatesearcher.exception;

/**
 * Created by phoenix on 25.10.14.
 */
public class DuplicateLineException extends IllegalArgumentException {
    public DuplicateLineException(String line) {
        super("Line has been already added: " + line);
    }
}
