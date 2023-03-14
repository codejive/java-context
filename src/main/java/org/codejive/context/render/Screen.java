package org.codejive.context.render;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.jline.utils.AttributedCharSequence;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;

public class Screen implements RectLike {
    private final AttributedStringBuilder[] lines;
    private final Rect rect;

    @Override
    public Rect rect() {
        return rect;
    }

    public Screen(int width, int height) {
        this.rect = new Rect(0, 0, width, height);
        lines = new AttributedStringBuilder[height];
        for (int i = 0; i < height; i++) {
            lines[i] = new AttributedStringBuilder(width);
        }
    }

    public void printAt(int x, int y, AttributedString str) {
        if (y < rect().top() || y > rect().bottom()) {
            return;
        }
        AttributedStringBuilder line = lines[y];
        if (x > rect().right() || (x + str.length() - 1) < rect().left()) {
            return;
        }
        if (x < rect().left()) {
            str = str.substring(rect().left() - x, str.length());
            x = rect().left();
        }
        if ((x + str.length() - 1) > rect().right()) {
            str = str.substring(0, rect().right() - x + 1);
        }
        if (line.length() < x) {
            pad(line, ' ', x - line.length());
            line.append(str);
        } else if (x + str.length() >= line.length()) {
            line.setLength(x);
            line.append(str);
        } else {
            AttributedStringBuilder ln = new AttributedStringBuilder(rect.width());
            ln.append(line.substring(0, x));
            ln.append(str);
            ln.append(line.substring(x + str.length(), line.length()));
            lines[y] = ln;
        }
    }

    private static void pad(AttributedStringBuilder str, char c, int n) {
        for (int i = 0; i < n; i++) {
            str.append(c);
        }
    }

    public List<AttributedString> lines() {
        return Arrays.stream(lines)
                .map(AttributedCharSequence::toAttributedString)
                .collect(Collectors.toList());
    }
}
