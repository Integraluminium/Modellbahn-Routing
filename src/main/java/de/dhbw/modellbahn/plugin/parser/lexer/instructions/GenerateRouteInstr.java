package de.dhbw.modellbahn.plugin.parser.lexer.instructions;

import de.dhbw.modellbahn.plugin.parser.lexer.CommandContext;

public record GenerateRouteInstr() implements Instruction {
    @Override
    public void execute(final CommandContext context) throws Exception {
        context.generateRoute();
    }

    @Override
    public void trace(final CommandContext context) {

    }
}
