package phoenix.templatesearcher.matchers;

import static java.lang.Math.max;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import phoenix.templatesearcher.api.ICharStream;
import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.api.IOccurence;
import phoenix.templatesearcher.support.Occurence;

public class NaiveTemplateMatcher implements IMetaTemplateMatcher {
    private List<char[]> templates;
    private List<Integer> hashCodes;

    public NaiveTemplateMatcher() {
	templates = new LinkedList<>();
	hashCodes = new LinkedList<>();
    }

    @Override
    public int addTemplate(String template)
	    throws UnsupportedOperationException {
	if (template.isEmpty()) {
	    throw new IllegalArgumentException("Template must not be empty");
	}
	
	int hashCode = template.hashCode();
	
	Iterator<char[]> templateIterator = templates.iterator();
	Iterator<Integer> hashCodeIterator = hashCodes.iterator();
	
	while (hashCodeIterator.hasNext()) {
	    int nextHashCode = hashCodeIterator.next();
	    char[] nextTemplate = templateIterator.next();
	    if (nextHashCode == hashCode && nextTemplate.length == template.length()) {
		boolean equal = true;
		for (int i = 0, len = template.length(); i < len; i++) {
		    if (nextTemplate[i] != template.charAt(i)) {
			equal = false;
			break;
		    }
		}
		
		if (equal) {
		    throw new IllegalArgumentException("Duplicate template given: " + template);
		}
	    }
	}
	
	templates.add(template.toCharArray());
	hashCodes.add(hashCode);
	return templates.size() - 1;
    }

    @Override
    public List<IOccurence> matchStream(ICharStream stream) {
	List<IOccurence> matches = new LinkedList<IOccurence>();

	int maxLength = 0;
	for (char[] template : templates) {
	    maxLength = max(maxLength, template.length);
	}

	/*
	 * Why do we use double length here? Suppose we use single length here,
	 * then we check the largest template once, shift array, read one more
	 * character, and again... - not effective.
	 * What is happening when
	 * we have double length: may n = single length, 2n is buffer length
	 * then. We perform n checks for O(n^2) then shift n characters and read
	 * n characters; Count of such "blocks" = <stream size> / n; Total time:
	 * <stream size> / n * (n^2 + 2n) = O(<stream size> * <largest template
	 * size) for each template; Then for all templates we can suppose
	 * O(<stream size> * <total template size>) asymptotic. The requirements
	 * are honoured well.
	 */
	maxLength *= 2;

	char[] buffer = new char[maxLength]; // read characters
	int pointer = 0; // first not set character position in buffer
	int readBefore = 0; // number of characters read before the characters
			    // in buffer

	// cycling through the buffer reading from stream if needed
	for (int begin = 0; begin < pointer || !stream.isEmpty(); begin++) {
	    // template id
	    int templID = 0;

	    for (char[] template : templates) {
		// shift buffer for reading more characters to perform
		// comparison
		if (template.length + begin > pointer && !stream.isEmpty()) {
		    int copySize = pointer - begin;
		    System.arraycopy(buffer, begin, buffer, 0, copySize);
		    pointer = copySize;
		    readBefore += begin;
		    begin = 0;
		}
		// reading portion of data
		for (; pointer < maxLength && !stream.isEmpty(); pointer++) {
		    buffer[pointer] = stream.nextChar();
		}

		// if template is too long, then it cannot match the suffix of
		// the stream we have in buffer.
		if (template.length + begin <= pointer) {
		    boolean doesMatch = true;
		    for (int i = begin, j = 0, len = template.length; j < len; i++, j++) {
			if (template[j] != buffer[i]) {
			    doesMatch = false;
			    break;
			}
		    }
		    if (doesMatch) {
			matches.add(new Occurence(readBefore + begin
				+ template.length - 1, templID));
		    }
		}

		templID++;
	    }

	}

	return matches;
    }
}
