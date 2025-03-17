package de.dhbw.modellbahn.plugin.parser.lexer.machines;

import de.dhbw.modellbahn.plugin.parser.lexer.TokenType;

public class LocIdMachine extends TokenMachine {
    public LocIdMachine() {
        super(TokenType.LOC_ID);
    }

    @Override
    public int scan(final CharSequence input, final int pos) {
        if (pos >= input.length() || !Character.isDigit(input.charAt(pos))) {
            return 0;
        }

        int currentPos = pos + 1;
        while (currentPos < input.length() && !Character.isWhitespace(input.charAt(currentPos))) {
            if (!Character.isDigit(input.charAt(currentPos))) {
                return 0;
            }
            currentPos++;
        }

        return currentPos - pos;
    }

}
