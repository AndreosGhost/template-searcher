package phoenix.templatesearcher.support;

import phoenix.templatesearcher.api.IOccurence;

public class Occurence extends ReadOnlyPair<Integer, Integer> implements
	IOccurence {
    public Occurence(int index, int templateID) {
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
    public boolean equals(Object obj) {
	if (obj instanceof IOccurence) {
	    IOccurence occ = (IOccurence) obj;
	    return getIndex() == occ.getIndex()
		    && getTemplateID() == occ.getTemplateID();
	} else {
	    return false;
	}
    }
    
    @Override
    public int hashCode() {
	return value * 1019 + key;
    }
    
    @Override
    public String toString() {
	return String.format("(%s, %s)", getIndex(), getTemplateID());
    }
}
