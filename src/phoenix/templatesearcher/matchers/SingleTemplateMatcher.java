package phoenix.templatesearcher.matchers;

import phoenix.templatesearcher.algo.PrefixFunction;
import phoenix.templatesearcher.api.ICharStream;
import phoenix.templatesearcher.api.IMetaTemplateMatcher;
import phoenix.templatesearcher.api.IOccurrence;
import phoenix.templatesearcher.support.ICharComparator;
import phoenix.templatesearcher.support.Occurrence;

import java.util.LinkedList;
import java.util.List;

public class SingleTemplateMatcher implements IMetaTemplateMatcher {
    protected ICharComparator comparator;
    private int templateLength;
    private char[] data;
    private int[] pi;
    private LinkedList<Character> appendedCharacters;
    private LinkedList<Character> prependedCharacters;

    /**
     * Creates an instance based on given char comparator
     * @param comparator
     *         object that can compare characters.
     */
    public SingleTemplateMatcher(ICharComparator comparator) {
        if (comparator == ICharComparator.WILDCARD_COMPARATOR) {
            throw new IllegalArgumentException(
                    "SingleTemplateMatcher does not work with wildcard characters");
        }
        this.comparator = comparator;
    }

    /**
     * Creates an instance based on default char comparator
     * @see {@link phoenix.templatesearcher.support.ICharComparator#DEFAULT_COMPARATOR }
     */
    public SingleTemplateMatcher() {
        this(ICharComparator.DEFAULT_COMPARATOR);
    }

    /**
     * Appends a character to the template.
     * @param ch
     *         Character to append.
     * @return This instance.
     * @throws UnsupportedOperationException
     *         If the template has not been added.
     */
    public SingleTemplateMatcher appendChar(char ch) throws UnsupportedOperationException {
        if (data == null) {
            throw new UnsupportedOperationException("Template not set yet");
        }
        appendedCharacters.add(ch);
        return this;
    }

    /**
     * Prepends a character to the template.
     * @param ch
     *         Character to prepend.
     * @return This instance.
     * @throws UnsupportedOperationException
     *         If the template has not been added.
     */
    public SingleTemplateMatcher prependChar(char ch) throws UnsupportedOperationException {
        if (data == null) {
            throw new UnsupportedOperationException("Template not set yet");
        }
        prependedCharacters.addFirst(ch);
        return this;
    }

    @Override
    public int addTemplate(String template) throws UnsupportedOperationException {
        if (template.isEmpty()) {
            throw new IllegalArgumentException("Template must not be empty");
        }
        if (this.data == null) {
            templateLength = template.length();

            data = new char[templateLength * 3 + 1];
            System.arraycopy(template.toCharArray(), 0, data, 0, templateLength);
            data[templateLength] = ICharComparator.SERVICE_SYMBOL;

            pi = new int[data.length];
            PrefixFunction.countPrefixFunction(data, pi, 1, templateLength, comparator);

            appendedCharacters = new LinkedList<>();
            prependedCharacters = new LinkedList<>();

            return 0;
        } else {
            throw new UnsupportedOperationException(
                    "Cannot add more then one template");
        }
    }

    @Override
    public List<IOccurrence> matchStream(ICharStream stream) {
        // delayed template update and prefix function rebuilding
        if (!appendedCharacters.isEmpty() || !prependedCharacters.isEmpty()) {
            int newTemplateLength =
                    templateLength + appendedCharacters.size() + prependedCharacters.size();
            char[] newData = new char[newTemplateLength * 3 + 1];
            int pointer = 0;

            for (Character ch : prependedCharacters) {
                newData[pointer++] = ch;
            }
            System.arraycopy(data, 0, newData, pointer, templateLength);
            pointer += templateLength;
            for (Character ch : appendedCharacters) {
                newData[pointer++] = ch;
            }
            newData[pointer] = ICharComparator.SERVICE_SYMBOL;

            data = newData;
            templateLength = newTemplateLength;
            appendedCharacters.clear();
            prependedCharacters.clear();

            pi = new int[data.length];
            PrefixFunction.countPrefixFunction(data, pi, 0, templateLength, comparator);
        }

        // ready to do the main task
        List<IOccurrence> results = new LinkedList<>();

        int readBefore = 0; // number of characters read before the characters
        // in buffer

        int bufferOffset = templateLength + 1; // start of stream data
        int pointer = bufferOffset; // first unset character position

        for (int dataLength = data.length; pointer < dataLength && !stream.isEmpty(); pointer++) {
            data[pointer] = stream.nextChar();
        }

        PrefixFunction.countPrefixFunction(data, pi, bufferOffset, pointer, comparator);

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
            for (int dataLength = data.length; pointer < dataLength && !stream.isEmpty();
                 pointer++) {
                data[pointer] = stream.nextChar();
            }

            // recounting pi function and adding matches
            PrefixFunction.countPrefixFunction(data, pi, newDataBegin, pointer, comparator);
            results.addAll(
                    findMatches(
                            newDataBegin, pointer, readBefore + copySize));
        }

        return results;
    }

    /**
     * Looks for matches in range [begin, end). Indices are shifted by {@code offset} value.
     * @param begin
     *         start of lookup range (inclusive)
     * @param end
     *         end of lookup range (exclusive)
     * @param offset
     *         offset to be added to shift from {@code begin}
     * @return list of occurrences
     */
    protected List<IOccurrence> findMatches(int begin, int end, int offset) {
        List<IOccurrence> results = new LinkedList<>();

        for (int i = begin; i < end; i++) {
            if (pi[i] == templateLength) {
                results.add(new Occurrence(i - begin + offset, 0));
            }
        }

        return results;
    }
}
