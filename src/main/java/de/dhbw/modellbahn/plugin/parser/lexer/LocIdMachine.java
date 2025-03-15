package de.dhbw.modellbahn.plugin.parser.lexer;

import de.dhbw.modellbahn.plugin.parser.lexer.machines.TokenMachine;

public class LocIdMachine extends TokenMachine {
    public LocIdMachine() {
        super(TokenType.LOC_ID);
    }

    @Override
    public int scan(final CharSequence input, final int pos) {
        if (input.length() - pos > 1) {
            int currentPos = pos + 1;
            while (currentPos < input.length() && Character.isDigit(input.charAt(currentPos))) {
                currentPos++;
            }
            return currentPos - pos;
        }
        return 0;
    }
}
