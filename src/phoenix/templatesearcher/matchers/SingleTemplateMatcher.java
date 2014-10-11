package phoenix.templatesearcher.matchers;

import java.util.LinkedList;
import java.util.List;

import phoenix.templatesearcher.api.ICharStream;
import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.api.IOccurence;
import phoenix.templatesearcher.support.Occurence;
import phoenix.templatesearcher.support.Utility;

public class SingleTemplateMatcher implements IMetaTemplateMatcher {
    private int templateLength;
    private char[] data;
    private int[] pi;

    @Override
    public int addTemplate(String template)
	    throws UnsupportedOperationException {
	if (this.data == null) {
	    templateLength = template.length();

	    data = new char[templateLength * 3 + 1];
	    System.arraycopy(template.toCharArray(), 0, data, 0, templateLength);
	    data[templateLength] = '\u0000';

	    pi = new int[data.length];
	    Utility.recountPrefixFunction(data, pi, 1, templateLength);

	    return 0;
	} else {
	    throw new UnsupportedOperationException(
		    "Cannot add more then one template");
	}
    }

    @Override
    public List<IOccurence> matchStream(ICharStream stream) {
	List<IOccurence> results = new LinkedList<IOccurence>();

	int readBefore = 0; // number of characters read before the characters
			    // in buffer

	int bufferOffset = templateLength + 1; // start of stream data
	int pointer = bufferOffset; // first unset character position

	for (int dataLength = data.length; pointer < dataLength
		&& !stream.isEmpty(); pointer++) {
	    data[pointer] = stream.nextChar();
	}

	Utility.recountPrefixFunction(data, pi, bufferOffset, pointer);

	results.addAll(findMatches(bufferOffset, pointer, readBefore));

	while (!stream.isEmpty()) {
	    // shifting data
	    int copyFrom = Math.max(pointer - templateLength, bufferOffset);
	    int copySize = pointer - copyFrom; // <= templateLength

	    if (copyFrom > bufferOffset) {
		System.arraycopy(data, copyFrom, data, bufferOffset, copySize);
		System.arraycopy(pi, copyFrom, pi, bufferOffset, copySize);

		readBefore += (copyFrom - 1) - bufferOffset + 1;
	    }

	    // reading data
	    int newDataBegin = bufferOffset + copySize;

	    pointer = newDataBegin;
	    for (int dataLength = data.length; pointer < dataLength
		    && !stream.isEmpty(); pointer++) {
		data[pointer] = stream.nextChar();
	    }

	    // recounting pi function and adding matches
	    Utility.recountPrefixFunction(data, pi, newDataBegin, pointer);
	    results.addAll(findMatches(newDataBegin, pointer, readBefore
		    + copySize));
	}

	return results;
    }

    /**
     * Looks for matches in range [begin, end). Indices are shifted by
     * {@code offset} value.
     * 
     * @param begin
     * @param end
     * @param offset
     * @return
     */
    private List<IOccurence> findMatches(int begin, int end, int offset) {
	List<IOccurence> results = new LinkedList<IOccurence>();

	for (int i = begin; i < end; i++) {
	    if (pi[i] == templateLength) {
		results.add(new Occurence(i - begin + offset, 0));
	    }
	}

	return results;
    }
}
