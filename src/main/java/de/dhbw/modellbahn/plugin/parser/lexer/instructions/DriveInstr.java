package de.dhbw.modellbahn.plugin.parser.lexer.instructions;

import de.dhbw.modellbahn.plugin.parser.lexer.CommandContext;

public class DriveInstr implements Instruction {
    @Override
    public void execute(final CommandContext context) throws Exception {
        context.getOutput().println("Execute DriveInstruction");
        context.getCurrentRouteBuilder().generateRoute();

        // TODO drive command
        context.resetRouteBuilder();
    }
}
