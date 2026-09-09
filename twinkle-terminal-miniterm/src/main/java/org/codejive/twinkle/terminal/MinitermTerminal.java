package org.codejive.twinkle.terminal;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.function.Consumer;
import org.codejive.miniterm.Terminal.Size;
import org.codejive.twinkle.terminal.io.InputReader;

public class MinitermTerminal implements Terminal {
    private final org.codejive.miniterm.Terminal terminal;
    private final Charset charset;
    private final InputReader inputReader;
    private final PrintWriter outputWriter;
    private final Thread inputThread;

    private volatile boolean closed;
    private Consumer<org.codejive.twinkle.text.Size> resizeCallback;

    public MinitermTerminal() throws IOException {
        terminal = org.codejive.miniterm.Terminal.create();
        charset = terminal.charset();
        inputReader = new InputReader();
        outputWriter = new PrintWriter(new OutputWriter(terminal), true);
        terminal.enableRawMode();
        terminal.onResize(this::handleResize);
        inputThread = new Thread(this::pumpInput, "twinkle-miniterm-input");
        inputThread.setDaemon(true);
        inputThread.start();
    }

    @Override
    public org.codejive.twinkle.text.Size size() {
        try {
            Size size = terminal.size();
            return org.codejive.twinkle.text.Size.of(size.width, size.height);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Terminal onResize(Consumer<org.codejive.twinkle.text.Size> resizeCallback) {
        this.resizeCallback = resizeCallback;
        return this;
    }

    @Override
    public Reader reader() {
        return inputReader;
    }

    @Override
    public PrintWriter writer() {
        return outputWriter;
    }

    @Override
    public void close() throws Exception {
        closed = true;
        inputThread.interrupt();
        inputReader.close();
        outputWriter.flush();
        terminal.disableRawMode();
        terminal.close();
    }

    private void handleResize(Size size) {
        Consumer<org.codejive.twinkle.text.Size> callback = resizeCallback;
        if (callback != null) {
            callback.accept(org.codejive.twinkle.text.Size.of(size.width, size.height));
        }
    }

    private void pumpInput() {
        while (!closed) {
            try {
                int ch = terminal.read(100);
                if (ch >= 0) {
                    inputReader.push(ch);
                } else if (ch == -1) {
                    break;
                }
            } catch (IOException e) {
                if (!closed) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
    }

    public static class Provider implements TerminalProvider {
        @Override
        public String name() {
            return "miniterm";
        }

        @Override
        public Terminal get() {
            try {
                return new MinitermTerminal();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class OutputWriter extends Writer {
        private final org.codejive.miniterm.Terminal terminal;

        OutputWriter(org.codejive.miniterm.Terminal terminal) {
            this.terminal = terminal;
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            terminal.write(new String(cbuf, off, len));
        }

        @Override
        public void flush() throws IOException {
            // miniterm flushes on write
        }

        @Override
        public void close() throws IOException {
            terminal.close();
        }
    }
}
