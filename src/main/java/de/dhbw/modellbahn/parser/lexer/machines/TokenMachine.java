package de.dhbw.modellbahn.parser.lexer.machines;

import de.dhbw.modellbahn.parser.lexer.TokenType;

public abstract class TokenMachine {
    protected final TokenType type;

    public TokenMachine(final TokenType type) {
        this.type = type;
    }

    public abstract int scan(CharSequence input, int pos);

    public TokenType getTokenType() {
        return type;
    }
}
