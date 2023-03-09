package org.codejive.context.styles;

import static org.codejive.context.styles.Type.AUTO;
import static org.codejive.context.styles.Type.INHERIT;
import static org.codejive.context.styles.Type.INITIAL;
import static org.codejive.context.styles.Type.UNSET;
import static org.codejive.context.styles.Type.length;
import static org.codejive.context.styles.Type.percentage;

public enum Property {
    left(false, length, percentage, INHERIT, INITIAL, UNSET),
    right(false, length, percentage, INHERIT, INITIAL, UNSET),
    top(false, length, percentage, INHERIT, INITIAL, UNSET),
    bottom(false, length, percentage, INHERIT, INITIAL, UNSET),
    width(false, length, percentage, AUTO, INHERIT, INITIAL, UNSET),
    height(false, length, percentage, AUTO, INHERIT, INITIAL, UNSET);

    public final Type[] types;
    public final boolean inherited;

    Property(boolean inherited, Type... types) {
        this.inherited = inherited;
        this.types = types;
    }
}
