package de.dhbw.modellbahn.plugin.parser.lexer;

public record Token(TokenType type, String value, int line, int column) {
}
