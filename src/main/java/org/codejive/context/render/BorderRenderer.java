package org.codejive.context.render;

import org.codejive.context.terminal.Rect;
import org.codejive.context.terminal.Screen;
import org.codejive.context.util.Util;
import org.jline.utils.AttributedString;

public class BorderRenderer {
    private final Screen screen;

    public BorderRenderer(Screen screen) {
        this.screen = screen;
    }

    public void render(Box box) {
        int lw = box.border_left_width();
        int tw = box.border_top_width();
        int rw = box.border_right_width();
        int bw = box.border_bottom_width();
        if (lw == 0 && tw == 0 && rw == 0 && bw == 0) {
            // Nothing to do
            return;
        }
        // Calculate the rectangle to draw the border in
        Rect r = box.rect().grow(lw, tw, rw, bw);
        if (r.outside(screen.rect())) {
            return;
        }

        int x = r.left();
        int y = r.top();
        int w = r.width();
        int h = r.height();
        AttributedString tbs = new AttributedString(String.format("+%s+", Util.repeat("-", w - 2)));
        AttributedString ins = new AttributedString(String.format("|%s|", Util.repeat(" ", w - 2)));
        screen.printAt(x, y, tbs);
        for (int i = 1; i < h - 1; i++) {
            screen.printAt(x, y + i, ins);
        }
        screen.printAt(x, y + h - 1, tbs);
    }
}
