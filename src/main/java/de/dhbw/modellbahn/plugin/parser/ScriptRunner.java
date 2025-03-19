package de.dhbw.modellbahn.plugin.parser;

import de.dhbw.modellbahn.plugin.parser.lexer.LexerException;
import de.dhbw.modellbahn.plugin.parser.lexer.instructions.Instruction;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ScriptRunner {
    private final CommandParser parser;
    private final CommandExecutor executor;
    private final PrintStream output;

    public ScriptRunner(CommandParser parser, CommandExecutor executor, PrintStream output) {
        this.parser = parser;
        this.executor = executor;
        this.output = output;
    }

    public void runScript(Path scriptPath) throws IOException {
        // Read entire file content
        String scriptContent = Files.readString(scriptPath);
        output.println("Executing script: " + scriptPath.getFileName());

        try {
            // Let the parser handle multiline commands and comments
            List<Instruction> instructions = parser.parse(scriptContent);
            executor.execute(instructions, true);
        } catch (Exception | LexerException e) {
            output.println("[Error] executing script: " + e.getMessage());
            e.printStackTrace(output);
        }
    }
}