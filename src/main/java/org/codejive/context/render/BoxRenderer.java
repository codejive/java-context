package org.codejive.context.render;

import org.jline.utils.AttributedString;

import java.util.List;

public class BoxRenderer {
    private final Screen screen;

    public BoxRenderer(Screen screen) {
        this.screen = screen;
    }

    public void render(List<Box> boxes) {
        for (Box b : boxes) {
            renderBox(b);
        }
    }

    private void renderBox(Box box) {
        if (box.outside(screen)) {
            return;
        }
        int x = box.left();
        int y = box.top();
        int i = 0;
        for (AttributedString str : box.content()) {
            screen.printAt(x, y + i, str);
            i++;
        }
    }
}
