package phoenix.templatesearcher.api;

/**
 * Interface for character stream. Characters can be extracted from it one by one.
 * @author phoenix
 */
public interface ICharStream {
    char nextChar();

    boolean isEmpty();
}
