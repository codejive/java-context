package org.codejive.context.render;

import org.codejive.context.styles.Property;
import org.codejive.context.styles.Style;
import org.codejive.context.styles.Unit;
import org.codejive.context.styles.Value.Len;
import org.jline.utils.AttributedString;

import java.util.List;

public class Box implements Rect {
    private final List<AttributedString> content;
    private final Style style;

    public Box(List<AttributedString> content) {
        this(content, new Style());
    }

    public Box(List<AttributedString> content, Style style) {
        this.content = content;
        this.style = style;
    }

    public List<AttributedString> content() {
        return content;
    }

    public Style style() {
        return style;
    }

    /**
     * Returns top position as a quantity of <code>Unit.em</code>
     * rounded to the nearest integer.
     * @return top position in em
     */
    @Override
    public int top() {
        return propAsEmInt(Property.top);
    }

    /**
     * Returns bottom position as a quantity of <code>Unit.em</code>
     * rounded to the nearest integer.
     * @return bottom position in em
     */
    @Override
    public int bottom() {
        return propAsEmInt(Property.bottom);
    }

    /**
     * Returns left position as a quantity of <code>Unit.em</code>
     * rounded to the nearest integer.
     * @return left position in em
     */
    @Override
    public int left() {
        return propAsEmInt(Property.left);
    }

    /**
     * Returns right position as a quantity of <code>Unit.em</code>
     * rounded to the nearest integer.
     * @return right position in em
     */
    @Override
    public int right() {
        return propAsEmInt(Property.right);
    }

    /**
     * Returns width as a quantity of <code>Unit.em</code>
     * rounded to the nearest integer.
     * @return width in em
     */
    @Override
    public int width() {
        return propAsEmInt(Property.width);
    }

    /**
     * Returns height as a quantity of <code>Unit.em</code>
     * rounded to the nearest integer.
     * @return height in em
     */
    @Override
    public int height() {
        return propAsEmInt(Property.height);
    }

    public int propAsEmInt(Property p) {
        return Math.round(style().get(p).as(Len.class).convert(Unit.em).get());
    }
}
