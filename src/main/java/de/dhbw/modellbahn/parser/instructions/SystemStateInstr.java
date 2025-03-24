package de.dhbw.modellbahn.parser.instructions;

import de.dhbw.modellbahn.parser.lexer.CommandContext;

public record SystemStateInstr(boolean toModifySystem) implements Instruction {
    @Override
    public void execute(final CommandContext context) throws Exception {
        if (toModifySystem) {
            context.getSystemCalls().systemGo();
            context.getOutput().println("System is being started");
        } else {
            context.getSystemCalls().systemStop();
            context.getOutput().println("System is being stopped");
        }
    }

    @Override
    public void trace(final CommandContext context) {
        context.getOutput().println(toModifySystem ? "System Start" : "System Stop");
    }
}
