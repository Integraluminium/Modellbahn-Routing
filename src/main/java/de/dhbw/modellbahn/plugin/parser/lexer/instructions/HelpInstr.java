package de.dhbw.modellbahn.plugin.parser.lexer.instructions;

import de.dhbw.modellbahn.plugin.parser.lexer.CommandContext;

import java.io.PrintStream;

public class HelpInstr implements Instruction {
    @Override
    public void execute(final CommandContext context) throws Exception {
        PrintStream output = context.getOutput();
        output.println("Help");
        output.println("----");
        output.println("Available Commands:");
        // TODO: Add all available commands
    }

    @Override
    public void trace(final CommandContext context) {
        context.getOutput().println("Help command");
    }
}
