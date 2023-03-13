package org.codejive.context.styles;

public enum Type {
    integer,
    length,
    number,
    percentage,
    string,
    INHERIT("inherit"),
    INITIAL("initial"),
    UNSET("unset"),
    AUTO("auto");

    String literal;

    Type() {}

    Type(String literal) {
        this.literal = literal;
    }
}
