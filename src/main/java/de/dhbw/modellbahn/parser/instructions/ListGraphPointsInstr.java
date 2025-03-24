package de.dhbw.modellbahn.parser.instructions;

import de.dhbw.modellbahn.parser.lexer.CommandContext;

public record ListGraphPointsInstr() implements Instruction {
    @Override
    public void execute(final CommandContext context) throws Exception {
        context.listAllGraphPoints();
    }

    @Override
    public void trace(final CommandContext context) {
        context.getOutput().println("Listing all graph points");
    }
}
