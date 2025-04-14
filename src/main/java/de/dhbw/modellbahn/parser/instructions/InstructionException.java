package de.dhbw.modellbahn.parser.instructions;

public class InstructionException extends RuntimeException {
    public InstructionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InstructionException(String message) {
        super(message);
    }
}
