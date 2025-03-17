package de.dhbw.modellbahn.plugin.parser.lexer.instructions;

import de.dhbw.modellbahn.plugin.parser.lexer.CommandContext;

public record ConsiderHeightInstr(boolean toConsider) implements Instruction {
    @Override
    public void execute(final CommandContext context) {
        context.getCurrentRouteBuilder().considerHeight(toConsider);
    }

    @Override
    public void trace(final CommandContext context) {
        context.getOutput().println("Consider Height: " + toConsider);
    }
}
