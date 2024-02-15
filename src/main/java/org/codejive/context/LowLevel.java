package org.codejive.context;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import org.codejive.context.render.BorderRenderer;
import org.codejive.context.render.Box;
import org.codejive.context.render.BoxRenderer;
import org.codejive.context.render.Screen;
import org.codejive.context.styles.Property;
import org.codejive.context.styles.Style;
import org.codejive.context.styles.Unit;
import org.codejive.context.styles.Value;
import org.codejive.context.util.ScrollBuffer;
import org.jline.terminal.Attributes;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.Display;
import org.jline.utils.InfoCmp.Capability;

public class LowLevel {
    private final Terminal terminal;

    public LowLevel(Terminal terminal) {
        this.terminal = terminal;
    }

    public static void main(String... args) throws IOException {
        try (Terminal terminal = TerminalBuilder.builder().build()) {
            Attributes savedAttributes = terminal.enterRawMode();
            try {
                new LowLevel(terminal).run();
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
        if (size.getColumns() == 0 && size.getRows() == 0) {
            size = new Size(80, 40);
        }
        int termWidth = size.getColumns();
        int termHeight = size.getRows();
        int displayHeight = 20;
        display.resize(displayHeight, termWidth);

        Box b1 = null, b2 = null, b3 = null, b4 = null;
        ScrollBuffer sb4 = new ScrollBuffer(5, 30, true);
        boolean refresh = true;
        int cnt = 0;
        out:
        while (true) {
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
            BorderRenderer br = new BorderRenderer(scr);
            BoxRenderer boxr = new BoxRenderer(scr);
            for (Box b : Arrays.asList(b1, b2, b3, b4)) {
                br.render(b);
                boxr.render(b);
            }
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
        AttributedStringBuilder cb = new AttributedStringBuilder();

        Integer cols = terminal.getNumericCapability(Capability.max_colors);
        if (cols != null) {
            int col = (int) (Math.random() * (cols + 1));
            cb.styled(cb.style().foreground(col), "012345678901234");
        } else {
            cb.append("012345678901234");
        }

        AttributedString c = cb.toAttributedString();
        Box b = new Box(Arrays.asList(c, c, c, c, c));
        setSize(b, 15, 5);
        setBorderWidth(b, 1);
        return b;
    }

    private Box createTimerBox(Style s) {
        AttributedStringBuilder cb = new AttributedStringBuilder();
        cb.append(Instant.now().toString());
        AttributedString c = cb.toAttributedString();
        Box b = new Box(Arrays.asList(c), s);
        setSize(b, 15, 1);
        setBorderWidth(b, 1);
        return b;
    }

    private Box createScrollBox(Style s, ScrollBuffer sb) {
        Box b = new Box(Arrays.asList(sb.getLines()), s);
        setSize(b, 30, 5);
        setBorderWidth(b, 1);
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

    private static void setBorderWidth(Box b, int w) {
        b.style().put(Property.border_width, Value.length(w, Unit.em));
    }
}
