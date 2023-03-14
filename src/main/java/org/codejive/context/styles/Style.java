package org.codejive.context.styles;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Style {
    protected final Map<Property, Value> propVals = new HashMap<>();

    public Value get(String p) {
        return propVals.get(Property.valueOf(p));
    }

    public Value get(String... ps) {
        return Arrays.stream(ps).map(this::get).filter(Objects::nonNull).findFirst().orElse(null);
    }

    public Value get(Property p) {
        return propVals.get(p);
    }

    public Value get(Property... ps) {
        return Arrays.stream(ps).map(this::get).filter(Objects::nonNull).findFirst().orElse(null);
    }

    public int getAsEmInt(String p) {
        return getAsEmInt(get(p));
    }

    public int getAsEmInt(Property p) {
        return getAsEmInt(get(p));
    }

    public int getAsEmInt(Value v) {
        return Math.round(v.as(Value.Len.class).convert(Unit.em).get());
    }

    public int getAsEmPosInt(String p) {
        return getAsEmPosInt(get(p));
    }

    public int getAsEmPosInt(Property p) {
        return getAsEmPosInt(get(p));
    }

    public int getAsEmPosInt(Value v) {
        return Math.max(Math.round(v.as(Value.Len.class).convert(Unit.em).get()), 0);
    }

    public Value put(String ps, String vs) {
        Property p = Property.valueOf(ps);
        Value v = Value.parse(vs).orElse(null);
        return put(p, v);
    }

    public Value put(Property p, Value v) {
        if (v != null) {
            return propVals.put(p, v);
        } else {
            return propVals.remove(p);
        }
    }

    public Set<Property> properties() {
        return propVals.keySet();
    }
}
