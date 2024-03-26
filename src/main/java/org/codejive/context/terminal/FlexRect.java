package org.codejive.context.terminal;

public class FlexRect {
    private final int left, top, width, height;

    public FlexRect(int left, int top, int width, int height) {
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
    }

    public Rect actualRect(Size availableSize) {
        Rect availableRect = new Rect(0, 0, availableSize.width(), availableSize.height());
        int w = width >= 0 ? width : availableSize.width() - left + width + 1;
        int h = height >= 0 ? height : availableSize.height() - top + height + 1;
        return new Rect(left, top, w, h).limited(availableRect);
    }
}
