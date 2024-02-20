package org.codejive.context.terminal;

import java.io.IOException;
import org.jline.utils.NonBlockingReader;

public interface Input {
    int readChar() throws IOException;

    int readChar(long timeout) throws IOException;
}

class InputImpl implements Input {
    private final Term term;
    private final NonBlockingReader reader;

    public InputImpl(Term term) {
        this.term = term;
        this.reader = term.terminal.reader();
    }

    @Override
    public int readChar() throws IOException {
        return readChar(0);
    }

    @Override
    public int readChar(long timeout) throws IOException {
        return reader.read(timeout);
    }
}
