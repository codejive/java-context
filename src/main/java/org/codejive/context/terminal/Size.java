package org.codejive.context.terminal;

public class Size {
    private final int width;
    private final int height;

    public Size(int width, int height) {
        assert width >= 0;
        assert height >= 0;
        this.width = width;
        this.height = height;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    @Override
    public String toString() {
        return width + "x" + height;
    }
}
