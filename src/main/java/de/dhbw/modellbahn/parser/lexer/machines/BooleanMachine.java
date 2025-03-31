package de.dhbw.modellbahn.parser.lexer.machines;

import de.dhbw.modellbahn.parser.lexer.TokenType;

public class BooleanMachine extends TokenMachine {
    public BooleanMachine() {
        super(TokenType.BOOLEAN);
    }


    @Override
    public int scan(final CharSequence input, final int pos) {
        if (pos >= input.length()) {
            return 0;
        }

        // Check for "true"
        if (matches(input, pos, "true")) {
            return pos + 4;
        }

        // Check for "false"
        if (matches(input, pos, "false")) {
            return pos + 5;
        }

        return 0;
    }

    private boolean matches(CharSequence input, int pos, String target) {
        if (pos + target.length() > input.length()) {
            return false;
        }

        for (int i = 0; i < target.length(); i++) {
            if (Character.toUpperCase(input.charAt(pos + i)) != Character.toUpperCase(target.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}
