package de.dhbw.modellbahn.parser.instructions;

import de.dhbw.modellbahn.parser.lexer.CommandContext;

public interface Instruction {
    void execute(CommandContext context) throws Exception;

    void trace(CommandContext context);
}
