package org.codejive.context;

import org.codejive.context.render.Box;
import org.codejive.context.render.BoxRenderer;
import org.codejive.context.render.Screen;
import org.codejive.context.styles.Property;
import org.codejive.context.styles.Style;
import org.codejive.context.styles.Type;
import org.codejive.context.styles.Unit;
import org.codejive.context.styles.Value;
import org.codejive.context.util.ScrollBuffer;
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
import java.time.Instant;
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

        Box b1 = null, b2 = null, b3 = null, b4 = null;
        ScrollBuffer sb4 = new ScrollBuffer(5, 30, true);
        boolean refresh = true;
        int cnt = 0;
        out: while (true) {
            if (refresh) {
                b1 = createColoredBox();
                setRandomPosSize(b1, termWidth, displayHeight);
                b2 = createTimerBox(new Style());
                setRandomPosSize(b2, termWidth, displayHeight);
                b3 = createColoredBox();
                setRandomPosSize(b3, termWidth, displayHeight);
                b4 = createScrollBox(new Style(), sb4);
                setRandomPosSize(b4, termWidth, displayHeight);
                refresh = false;
            }
            Screen scr = new Screen(termWidth, displayHeight);
            BoxRenderer r = new BoxRenderer(scr);
            r.render(Arrays.asList(b1, b2, b3, b4));
            display.update(scr.lines(), 0);
            int c = terminal.reader().read(100);
            if (c == 'q') {
                break out;
            } else if (c != -2) {
                refresh = true;
            } else {
                b2 = createTimerBox(b2.style());
                if (cnt % 5 == 0) {
                    sb4.append("Hello World! ");
                    b4 = createScrollBox(b4.style(), sb4);
                }
            }
            cnt++;
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
        setSize(b, 15, 5);
        return b;
    }

    private Box createTimerBox(Style s) {
        AttributedStringBuilder cb = new AttributedStringBuilder();
        cb.append(Instant.now().toString());
        AttributedString c = cb.toAttributedString();
        Box b = new Box(Arrays.asList(c), s);
        setSize(b, 15, 1);
        return b;
    }

    private Box createScrollBox(Style s, ScrollBuffer sb) {
        Box b = new Box(Arrays.asList(sb.getLines()), s);
        setSize(b, 30, 5);
        return b;
    }

    private static void setSize(Box b, int w, int h) {
        Style s = b.style();
        s.put(Property.width, Value.length(w, Unit.em));
        s.put(Property.height, Value.length(h, Unit.em));
    }

    private static void setRandomPosSize(Box b, int totalW, int totalH) {
        int minx = -b.width();
        int maxx = totalW + b.width();
        int miny = -b.height();
        int maxy = totalH + b.height();
        int x = (int) (Math.random() * (maxx - minx + 1) + minx);
        int y = (int) (Math.random() * (maxy - miny + 1) + miny);
        setPos(b, x, y);
    }

    private static void setPos(Box b, int x, int y) {
        int w = b.width();
        int h = b.height();
        Style s = b.style();
        s.put(Property.top, Value.length(y, Unit.em));
        s.put(Property.bottom, Value.length(y + w - 1, Unit.em));
        s.put(Property.left, Value.length(x, Unit.em));
        s.put(Property.right, Value.length(x + h - 1, Unit.em));
        s.put(Property.width, Value.length(w, Unit.em));
        s.put(Property.height, Value.length(h, Unit.em));
    }
}
