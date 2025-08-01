package de.dhbw.modellbahn.parser.lexer.machines;

import de.dhbw.modellbahn.parser.lexer.TokenType;

public class LineCommentMachine extends TokenMachine {

    public LineCommentMachine() {
        super(TokenType.COMMENT);
    }

    @Override
    public int scan(final CharSequence input, final int pos) {
        if (input.length() - pos > 2 && input.charAt(pos) == '/' && input.charAt(pos + 1) == '/') {
            int currentPos = pos + 2;

            // Continue until we hit end of line or end of input
            while (currentPos < input.length() && input.charAt(currentPos) != '\n') {
                currentPos++;
            }
            return currentPos - pos;
        } else if (input.length() > pos && input.charAt(pos) == '#') {
            int currentPos = pos + 1;
            // Continue until we hit end of line or end of input
            while (currentPos < input.length() && input.charAt(currentPos) != '\n') {
                currentPos++;
            }
            return currentPos - pos;
        }

        // Not a comment
        return 0;
    }
}
