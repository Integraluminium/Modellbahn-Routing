package de.dhbw.modellbahn.parser.lexer.machines;

import de.dhbw.modellbahn.application.routing.building.RoutingOptimization;
import de.dhbw.modellbahn.parser.lexer.TokenType;

public class OptimizationMachine extends TokenMachine {
    public OptimizationMachine() {
        super(TokenType.OPTIMIZATION);
    }

    @Override
    public int scan(final CharSequence input, final int pos) {
        if (pos >= input.length()) {
            return 0;
        }

        // Try to match any of the optimization enum values
        for (RoutingOptimization opt : RoutingOptimization.values()) {
            String optName = opt.name();

            // Check if there's enough input remaining
            if (pos + optName.length() > input.length()) {
                continue;
            }

            // Check if the sequence matches our optimization name
            boolean matches = true;
            for (int i = 0; i < optName.length(); i++) {
                // Case-insensitive comparison
                if (Character.toUpperCase(input.charAt(pos + i)) != optName.charAt(i)) {
                    matches = false;
                    break;
                }
            }

            if (matches) {
                // Ensure this is a whole word
                if (pos + optName.length() < input.length()) {
                    char nextChar = input.charAt(pos + optName.length());
                    if (Character.isLetterOrDigit(nextChar) || nextChar == '_') {
                        continue; // Not a complete word boundary
                    }
                }

                // Found a match, return its length
                return optName.length();
            }
        }

        return 0; // No match found
    }
}