package de.dhbw.modellbahn.parser.lexer.machines;

import de.dhbw.modellbahn.parser.lexer.TokenType;

public class FilePathMachine extends TokenMachine {

    public FilePathMachine() {
        super(TokenType.FILE_PATH);
    }

    @Override
    public int scan(final CharSequence input, final int pos) {
        if (pos >= input.length()) {
            return 0;
        }

        int currentPos = pos;
        boolean hasDriveLetter = false;

        // Check for drive letter pattern (like C:)
        if (currentPos + 1 < input.length() &&
                Character.isLetter(input.charAt(currentPos)) &&
                input.charAt(currentPos + 1) == ':') {
            currentPos += 2;
            hasDriveLetter = true;
        }

        // If no drive letter, check for absolute path starting with /
        if (!hasDriveLetter && currentPos < input.length() && input.charAt(currentPos) == '/') {
            currentPos++;
        }

        // Track start of the scanning position
        int startScan = currentPos;

        // Scan until we find an invalid character for a path
        while (currentPos < input.length() && isValidPathChar(input.charAt(currentPos))) {
            currentPos++;
        }

        // file path end
        if (currentPos >= input.length() || !(input.charAt(currentPos) == '.')) {
            return 0;
        }
        currentPos++;
        while (currentPos < input.length() && Character.isLetterOrDigit(input.charAt(currentPos))) {
            currentPos++;
        }

        // If we didn't advance, no valid path found
        if (currentPos == startScan && !hasDriveLetter) {
            return 0;
        }


        int matchLength = currentPos - pos;
        return Math.max(matchLength, 0);
    }

    private boolean isValidPathChar(char c) {
        return Character.isLetterOrDigit(c) || c == '/' || c == '\\' || c == '_' || c == '-';
    }
}
