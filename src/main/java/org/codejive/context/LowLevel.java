package org.codejive.context;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import org.codejive.context.render.BorderRenderer;
import org.codejive.context.render.Box;
import org.codejive.context.render.BoxRenderer;
import org.codejive.context.styles.Property;
import org.codejive.context.styles.Style;
import org.codejive.context.styles.Unit;
import org.codejive.context.styles.Value;
import org.codejive.context.terminal.Screen;
import org.codejive.context.terminal.Term;
import org.codejive.context.util.ScrollBuffer;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;

public class LowLevel {
    private final Term term;

    public LowLevel(Term term) {
        this.term = term;
    }

    public static void main(String... args) throws IOException {
        try (Term terminal = Term.create()) {
            new LowLevel(terminal).run();
        }
    }

    public int run() throws IOException {
        Screen screen = term.fullScreen();
        int displayWidth = screen.rect().width();
        int displayHeight = screen.rect().height();

        Box b1 = null, b2 = null, b3 = null, b4 = null;
        ScrollBuffer sb4 = new ScrollBuffer(5, 30, true);
        boolean refresh = true;
        int cnt = 0;
        out:
        while (true) {
            if (refresh) {
                b1 = createColoredBox();
                setRandomPosSize(b1, displayWidth, displayHeight);
                b2 = createTimerBox(new Style());
                setRandomPosSize(b2, displayWidth, displayHeight);
                b3 = createColoredBox();
                setRandomPosSize(b3, displayWidth, displayHeight);
                b4 = createScrollBox(new Style(), sb4);
                setRandomPosSize(b4, displayWidth, displayHeight);
                refresh = false;
            }
            BorderRenderer br = new BorderRenderer(screen);
            BoxRenderer boxr = new BoxRenderer(screen);
            for (Box b : Arrays.asList(b1, b2, b3, b4)) {
                br.render(b);
                boxr.render(b);
            }
            screen.update();
            int c = term.input().readChar(100);
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

        term.flush();
        return 0;
    }

    private Box createColoredBox() {
        AttributedStringBuilder cb = new AttributedStringBuilder();

        Integer cols = term.maxColors();
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
