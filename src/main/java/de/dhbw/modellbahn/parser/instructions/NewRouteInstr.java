package de.dhbw.modellbahn.parser.instructions;

import de.dhbw.modellbahn.parser.lexer.CommandContext;

public record NewRouteInstr() implements Instruction {
    @Override
    public void execute(final CommandContext context) {
        context.resetRouteBuilder();
    }

    @Override
    public void trace(final CommandContext context) {
        context.getOutput().println("Reset RouteBuilder");
    }
}
