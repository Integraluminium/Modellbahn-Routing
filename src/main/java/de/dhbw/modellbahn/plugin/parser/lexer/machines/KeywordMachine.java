package de.dhbw.modellbahn.plugin.parser.lexer.machines;

import de.dhbw.modellbahn.plugin.parser.lexer.TokenType;

public class KeywordMachine extends TokenMachine {
    private final String keyword;

    public KeywordMachine(final String keyword, final TokenType type) {
        super(type);
        this.keyword = keyword;
    }

    @Override
    public int scan(final CharSequence input, final int pos) {
        // Check if there's enough input left to match the keyword
        if (pos + keyword.length() > input.length()) {
            return 0;
        }

        // Check if the sequence matches our keyword
        for (int i = 0; i < keyword.length(); i++) {
            if (Character.toUpperCase(input.charAt(pos + i)) != keyword.charAt(i)) {
                return 0; // No match
            }
        }

        // Ensure this is a whole word (not part of a larger identifier)
        if (pos + keyword.length() < input.length()) {
            char nextChar = input.charAt(pos + keyword.length());
            if (Character.isLetterOrDigit(nextChar) || nextChar == '_') {
                return 0; // Not a complete word boundary
            }
        }

        // Found a match, return its length
        return keyword.length();
    }
}
