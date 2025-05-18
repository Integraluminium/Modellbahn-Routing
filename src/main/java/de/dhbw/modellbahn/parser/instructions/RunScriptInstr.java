package de.dhbw.modellbahn.parser.instructions;

import de.dhbw.modellbahn.parser.CommandParser;
import de.dhbw.modellbahn.parser.lexer.CommandContext;
import de.dhbw.modellbahn.parser.lexer.LexerException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public record RunScriptInstr(File file, CommandParser parser) implements Instruction {
    @Override
    public void execute(final CommandContext context) throws InstructionException {
        try {
            String scriptContent = Files.readString(file.toPath());
            List<Instruction> instructions = parser.parse(scriptContent);
            context.runScript(instructions);
        } catch (IOException | LexerException e) {
            throw new InstructionException("Error reading script file: " + file.getAbsolutePath() + " - " + e.getMessage());
        } catch (Exception e) {
            throw new InstructionException("Error executing script: " + e.getMessage());
        }
    }

    @Override
    public void trace(final CommandContext context) {
        context.getOutput().println("Executing script: " + file.getAbsolutePath());
    }
}
