package de.dhbw.modellbahn.plugin.parser.lexer.instructions;

import de.dhbw.modellbahn.plugin.parser.lexer.CommandContext;

public record NewRouteInstr() implements de.dhbw.modellbahn.plugin.parser.lexer.instructions.Instruction {
    @Override
    public void execute(final CommandContext context) throws Exception {
        context.resetRouteBuilder();
    }

    @Override
    public void trace(final CommandContext context) {
        context.getOutput().println("Reset RouteBuilder");
    }
}
