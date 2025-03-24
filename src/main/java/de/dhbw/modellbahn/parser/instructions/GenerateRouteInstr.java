package de.dhbw.modellbahn.parser.instructions;

import de.dhbw.modellbahn.parser.lexer.CommandContext;

public record GenerateRouteInstr() implements Instruction {
    @Override
    public void execute(final CommandContext context) throws Exception {
        context.generateRoute();
    }

    @Override
    public void trace(final CommandContext context) {

    }
}
