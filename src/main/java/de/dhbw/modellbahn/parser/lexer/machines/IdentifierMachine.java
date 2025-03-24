package de.dhbw.modellbahn.parser.lexer.machines;

import de.dhbw.modellbahn.parser.lexer.TokenType;

public class IdentifierMachine extends TokenMachine {
    public IdentifierMachine() {
        super(TokenType.STRING);
    }

    @Override
    public int scan(final CharSequence input, final int pos) {
        if (pos >= input.length()) {
            return 0;
        }

        if (!Character.isLetter(input.charAt(pos))) {
            return 0;
        }

        int currentPos = pos + 1;
        while (currentPos < input.length() && isValidGraphPointChar(input.charAt(currentPos))) {
            currentPos++;
        }

        // Length of the matched sequence
        int matchLength = currentPos - pos;

        // Only return non-zero if we matched at least one valid character
        return Math.max(matchLength, 0);
    }

    private boolean isValidGraphPointChar(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }
}
