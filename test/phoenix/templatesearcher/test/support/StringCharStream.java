package phoenix.templatesearcher.test.support;

import phoenix.templatesearcher.api.ICharStream;

public class StringCharStream implements ICharStream {
    private char[] stream;
    private int position;
    
    public StringCharStream(String str) {
	this.stream = str.toCharArray();
	this.position = 0;
    }
    
    @Override
    public boolean isEmpty() {
	return position >= stream.length;
    }

    @Override
    public char nextChar() {
	return stream[position++];
    }
}
