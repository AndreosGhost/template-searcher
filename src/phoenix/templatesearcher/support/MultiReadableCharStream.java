package phoenix.templatesearcher.support;

import phoenix.templatesearcher.api.ICharStream;

/**
 * Wrapping over {@link phoenix.templatesearcher.api.ICharStream} that makes possible multiple
 * rereading of the stream.
 */
public class MultiReadableCharStream implements ICharStream {
    private String streamData;
    private int pos;

    public MultiReadableCharStream(ICharStream stream) {
        StringBuilder sb = new StringBuilder();

        while (!stream.isEmpty()) {
            sb.append(stream.nextChar());
        }

        streamData = sb.toString();
        pos = 0;
    }

    @Override
    public boolean isEmpty() {
        return pos >= streamData.length();
    }

    @Override
    public char nextChar() {
        return streamData.charAt(pos++);
    }

    /**
     * Reset current position to the beginning of the stream, so that {@link #nextChar()} will
     * return the first symbol in the stream.
     */
    public void resetPosition() {
        pos = 0;
    }

    /**
     * Returns number of characters in the stream.
     * @return
     */
    public int streamSize() {
        return streamData.length();
    }
}
