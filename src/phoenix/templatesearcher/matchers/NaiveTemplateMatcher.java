package phoenix.templatesearcher.matchers;

import phoenix.templatesearcher.algo.FinalPineNode;
import phoenix.templatesearcher.algo.PineForest;
import phoenix.templatesearcher.api.ICharStream;
import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.api.IOccurrence;
import phoenix.templatesearcher.support.Occurrence;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.*;

public class NaiveTemplateMatcher implements IMetaTemplateMatcher {
    private PineForest<FinalPineNode> templatesForest;

    private int forestTemplatesCount;

    private String[] templates;

    public NaiveTemplateMatcher() {
        templatesForest = new PineForest<>(new FinalPineNode());
        forestTemplatesCount = 0;
    }

    @Override
    public int addTemplate(String template) throws UnsupportedOperationException, IllegalArgumentException {
        Objects.requireNonNull(template, "Template must not be null");
        if (template.isEmpty()) {
            throw new IllegalArgumentException("Template must not be empty");
        }

        // An exception can occur inside.
        templatesForest.addLine(template.toCharArray(), forestTemplatesCount);

        // If the template was really added.
        templates = null;
        return forestTemplatesCount++;
    }

    @Override
    public List<IOccurrence> matchStream(ICharStream stream) {
        if (templates == null) {
            rebuildTemplatesArray();
        }

        List<IOccurrence> matches = new LinkedList<>();

        int maxLength = 0;
        for (String template : templates) {
            maxLength = max(maxLength, template.length());
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
            int templateID = 0;

            for (String template : templates) {
                // shift buffer for reading more characters to perform
                // comparison
                if (template.length() + begin > pointer && !stream.isEmpty()) {
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
                if (template.length() + begin <= pointer) {
                    boolean doesMatch = true;
                    for (int i = begin, j = 0, len = template.length(); j < len; i++, j++) {
                        if (template.charAt(j) != buffer[i]) {
                            doesMatch = false;
                            break;
                        }
                    }
                    if (doesMatch) {
                        matches.add(
                                new Occurrence(
                                        readBefore + begin + template.length() - 1, templateID));
                    }
                }

                templateID++;
            }

        }

        return matches;
    }

    private void rebuildTemplatesArray() {
        templates = templatesForest.listLines();

        if (templates.length != forestTemplatesCount) {
            throw new IllegalStateException("Template counts do not match. Implementation error");
        }
    }
}
