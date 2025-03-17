package de.dhbw.modellbahn.plugin.parser.lexer.instructions;

import de.dhbw.modellbahn.plugin.parser.lexer.CommandContext;

public interface Instruction {
    void execute(CommandContext context) throws Exception;

    void trace(CommandContext context);
}
