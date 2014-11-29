package phoenix.templatesearcher.support;

import phoenix.templatesearcher.api.IOccurrence;

import java.util.Objects;

public class Occurrence extends ReadOnlyPair<Integer, Integer> implements IOccurrence {
    public Occurrence(int index, int templateID) {
        super(index, templateID);
    }

    public int getIndex() {
        return key;
    }

    public int getTemplateID() {
        return value;
    }

    @Deprecated
    @Override
    public Integer getKey() {
        return super.getKey();
    }

    @Deprecated
    @Override
    public Integer getValue() {
        return super.getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IOccurrence) {
            IOccurrence occ = (IOccurrence) obj;
            return getIndex() == occ.getIndex() && getTemplateID() == occ.getTemplateID();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", getIndex(), getTemplateID());
    }
}
