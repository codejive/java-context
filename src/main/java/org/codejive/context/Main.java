package org.codejive.context;

import org.codejive.context.render.Box;
import org.codejive.context.render.BoxRenderer;
import org.codejive.context.render.Screen;
import org.codejive.context.styles.Property;
import org.codejive.context.styles.Style;
import org.codejive.context.styles.Unit;
import org.codejive.context.styles.Value;
import org.fusesource.jansi.Ansi;
import org.jline.terminal.Attributes;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.Display;
import org.jline.utils.InfoCmp.Capability;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Main {
    private final Terminal terminal;

    public Main(Terminal terminal) {
        this.terminal = terminal;
    }

    public static void main(String... args) throws IOException {
        try (Terminal terminal = TerminalBuilder.builder().build()) {
            Attributes savedAttributes = terminal.enterRawMode();
            try {
                new Main(terminal).run();
            } finally {
                terminal.setAttributes(savedAttributes);
            }
        }
//        System.out.println("The next line is a lie!");
//        System.out.print(Ansi.ansi().cursorUpLine());
//        System.out.println("This is a working test!");
    }

    public int run() throws IOException {
        Display display = new Display(terminal, false);
        Size size = new Size();
        size.copy(terminal.getSize());

        int termWidth = size.getColumns();
        int termHeight = size.getRows();
        int displayHeight = 10;
        display.resize(displayHeight, termWidth);

        while (true) {
            Box b = createColoredBox();
            setRandomPosSize(b.style(), termWidth, displayHeight, 15, 5);
            Screen scr = new Screen(termWidth, displayHeight);
            BoxRenderer r = new BoxRenderer(scr);
            r.render(Arrays.asList(b));
            display.update(scr.lines(), 0);
            if (terminal.reader().read() == 'q') {
                break;
            }
            System.out.flush();
        }

        terminal.writer().flush();
        return 0;
    }

    private Box createColoredBox() {
        int cols = terminal.getNumericCapability(Capability.max_colors);
        int col = (int) (Math.random() * (cols + 1));
        AttributedStringBuilder cb = new AttributedStringBuilder();
        cb.styled(cb.style().foreground(col), "012345678901234");
        AttributedString c = cb.toAttributedString();
        Box b = new Box(Arrays.asList(c, c, c, c, c));
        return b;
    }

    private static void setRandomPosSize(Style s, int totalW, int totalH, int w, int h) {
        int minx = -16;
        int maxx = totalW + 16;
        int miny = -6;
        int maxy = totalH + 6;
        int x = (int) (Math.random() * (maxx - minx + 1) + minx);
        int y = (int) (Math.random() * (maxy - miny + 1) + miny);
        setPosSize(s, x, y, w, h);
    }

    private static void setPosSize(Style s, int x, int y, int w, int h) {
        s.put(Property.top, Value.length(y, Unit.em));
        s.put(Property.bottom, Value.length(y + w - 1, Unit.em));
        s.put(Property.left, Value.length(x, Unit.em));
        s.put(Property.right, Value.length(x + h - 1, Unit.em));
        s.put(Property.width, Value.length(w, Unit.em));
        s.put(Property.height, Value.length(h, Unit.em));
    }
}
