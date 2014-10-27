package phoenix.templatesearcher.support;

/**
 * Convenience class for storing a string with its unique id.
 */
public class IdentifiedLine extends ReadOnlyPair<String, Integer> {
    public IdentifiedLine(String line, int id) {
        super(line, id);
    }

    public String getLine() {
        return super.getKey();
    }

    public int getID() {
        return super.getValue();
    }

    @Deprecated
    @Override
    public String getKey() {
        return super.getKey();
    }

    @Deprecated
    @Override
    public Integer getValue() {
        return super.getValue();
    }
}
