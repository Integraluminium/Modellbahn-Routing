package de.dhbw.modellbahn.plugin.parser.lexer.machines;

import de.dhbw.modellbahn.plugin.parser.lexer.TokenType;

public class GraphPointMachine extends TokenMachine {
    public GraphPointMachine() {
        super(TokenType.GRAPH_POINT);
    }

    @Override
    public int scan(final CharSequence input, final int pos) {
        if (pos >= input.length()) {
            return 0;
        }

        int currentPos = pos;
        while (currentPos < input.length() && isValidGraphPointChar(input.charAt(currentPos))) {
            currentPos++;
        }

        // Length of the matched sequence
        int matchLength = currentPos - pos;

        // Only return non-zero if we matched at least one valid character
        return Math.max(matchLength, 0);
    }

    private boolean isValidGraphPointChar(char c) {
        return (c >= 'A' && c <= 'Z') ||  // Uppercase letters
                (c >= 'a' && c <= 'z') ||  // Lowercase letters
                (c >= '1' && c <= '9') ||  // Digits 1-9 (excluding 0)
                c == '_';                  // Underscore
    }
}
