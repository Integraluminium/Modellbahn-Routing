package de.dhbw.modellbahn.plugin.parser.lexer.instructions;

import de.dhbw.modellbahn.plugin.parser.lexer.CommandContext;

public record ConsiderElectrificationInstr(boolean toConsider) implements Instruction {

    @Override
    public void execute(final CommandContext context) {
        context.getCurrentRouteBuilder().considerElectrification(toConsider);
    }
}
