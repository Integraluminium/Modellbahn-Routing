package de.dhbw.modellbahn.parser.lexer;

import java.util.StringJoiner;

public record Token(TokenType type, String value, int line, int column) {
    @Override
    public String toString() {
        return new StringJoiner(", ", Token.class.getSimpleName() + "[", "]")
                .add("type=" + type)
                .add("value='" + value + "'")
                .add("line=" + line)
                .add("column=" + column)
                .toString();
    }
}
