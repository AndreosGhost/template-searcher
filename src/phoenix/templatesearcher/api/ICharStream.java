package phoenix.templatesearcher.api;

/**
 * Interface for character stream. Characters can be extracted from it one by one.
 * @author phoenix
 */
public interface ICharStream {
    public char nextChar();

    public boolean isEmpty();
}
