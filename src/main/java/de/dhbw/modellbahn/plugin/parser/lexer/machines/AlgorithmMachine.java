package de.dhbw.modellbahn.plugin.parser.lexer.machines;

import de.dhbw.modellbahn.application.RoutingAlgorithm;
import de.dhbw.modellbahn.plugin.parser.lexer.TokenType;

public class AlgorithmMachine extends TokenMachine {
    public AlgorithmMachine() {
        super(TokenType.ALGORITHM);
    }

    @Override
    public int scan(final CharSequence input, final int pos) {
        if (pos >= input.length()) {
            return 0;
        }

        // Try to match any of the optimization enum values
        for (RoutingAlgorithm routingAlgorithm : RoutingAlgorithm.values()) {
            String algName = routingAlgorithm.name();

            // Check if there's enough input remaining
            if (pos + algName.length() > input.length()) {
                continue;
            }
            
            boolean matches = true;
            for (int i = 0; i < algName.length(); i++) {
                // Case-insensitive comparison
                if (Character.toUpperCase(input.charAt(pos + i)) != Character.toUpperCase(algName.charAt(i))) {
                    matches = false;
                    break;
                }
            }

            if (matches) {
                // Ensure this is a whole word
                if (pos + algName.length() < input.length()) {
                    char nextChar = input.charAt(pos + algName.length());
                    if (Character.isLetterOrDigit(nextChar) || nextChar == '_') {
                        continue; // Not a complete word boundary
                    }
                }

                // Found a match, return its length
                return algName.length();
            }
        }
        return 0; // No match found
    }
}
