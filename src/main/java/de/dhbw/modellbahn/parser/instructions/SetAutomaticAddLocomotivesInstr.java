package de.dhbw.modellbahn.parser.instructions;

import de.dhbw.modellbahn.parser.lexer.CommandContext;

public record SetAutomaticAddLocomotivesInstr(boolean value) implements Instruction {
    @Override
    public void execute(final CommandContext context) {
        context.setAutomaticallyAddAllLocomotivesToRoute(value);
    }

    @Override
    public void trace(final CommandContext context) {
        context.getOutput().println("SetAutomaticAddLocomotivesInstr: " + value);
    }
}
