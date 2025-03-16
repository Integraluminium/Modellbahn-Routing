package de.dhbw.modellbahn.plugin.parser.lexer.instructions;

import de.dhbw.modellbahn.plugin.parser.lexer.CommandContext;

public class DriveInstr implements Instruction {
    @Override
    public void execute(final CommandContext context) throws Exception {
        context.getCurrentRouteBuilder().generateRoute();

        // TODO drive command
        context.resetRouteBuilder();
    }

    @Override
    public void trace(final CommandContext context) {
        context.getOutput().println("Execute DriveInstruction");
    }
}
