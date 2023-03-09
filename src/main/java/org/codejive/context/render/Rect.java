package org.codejive.context.render;

public interface Rect {
    default int top() {
        return bottom() - height() + 1;
    }

    default int bottom() {
        return top() + height() - 1;
    }

    default int left() {
        return right() - width() + 1;
    }

    default int right() {
        return left() + width() - 1;
    }

    default int width() {
        return right() - left() + 1;
    }

    default int height() {
        return bottom() - top() + 1;
    }

    static Rect sized(int width, int height) {
        return new RectImpl(0, 0, width, height);
    }

    static Rect sized(int left, int top, int width, int height) {
        return new RectImpl(left, top, width, height);
    }

    static Rect rect(int left, int top, int right, int bottom) {
        return new RectImpl(left, top, right - left + 1, bottom - top + 1);
    }

    default boolean outside(Rect other) {
        return top() > other.bottom() || bottom() < other.top() || left() > other.right() || right() < other.left();
    }

    default boolean inside(Rect other) {
        return top() >= other.top() && left() >= other.left() && bottom() <= other.bottom() && right() <= other.right();
    }

    default boolean overlap(Rect other) {
        return !outside(other) && !inside(other);
    }

    interface Wrapped extends Rect {

        Rect rect();

        default int top() {
            return rect().top();
        }

        default int bottom() {
            return rect().bottom();
        }

        default int left() {
            return rect().left();
        }

        default int right() {
            return rect().right();
        }

        default int width() {
            return rect().width();
        }

        default int height() {
            return rect().height();
        }
    }
}

class RectImpl implements Rect {
    private final int left, top, width, height;

    RectImpl(int left, int top, int width, int height) {
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
    }

    @Override
    public int left() {
        return left;
    }

    @Override
    public int top() {
        return top;
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }
}
