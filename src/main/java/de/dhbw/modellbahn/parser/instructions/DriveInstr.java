package de.dhbw.modellbahn.parser.instructions;

import de.dhbw.modellbahn.parser.lexer.CommandContext;

public class DriveInstr implements Instruction {
    @Override
    public void execute(final CommandContext context) throws InstructionException {
        context.driveRoute();
    }

    @Override
    public void trace(final CommandContext context) {
        context.getOutput().println("Execute DriveInstruction");
    }
}
