package org.codejive.context.styles;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Style {
    protected final Map<Property, Value> propVals =  new HashMap<>();

    public Value get(String p) {
        return propVals.get(Property.valueOf(p));
    }

    public Value get(Property p) {
        return propVals.get(p);
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
