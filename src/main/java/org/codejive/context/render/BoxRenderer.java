package org.codejive.context.render;

import org.codejive.context.terminal.Screen;
import org.jline.utils.AttributedString;

public class BoxRenderer {
    private final Screen screen;

    public BoxRenderer(Screen screen) {
        this.screen = screen;
    }

    public void render(Box box) {
        if (box.rect().outside(screen.rect())) {
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
