package de.dhbw.modellbahn.parser;

import de.dhbw.modellbahn.RoutingApplication;
import de.dhbw.modellbahn.parser.instructions.Instruction;
import de.dhbw.modellbahn.parser.lexer.LexerException;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ScriptRunner {
    private final CommandParser parser;
    private final RoutingApplication app;
    private final PrintStream output;

    public ScriptRunner(CommandParser parser, RoutingApplication app, PrintStream output) {
        this.parser = parser;
        this.app = app;
        this.output = output;
    }

    public void runScript(Path scriptPath) throws IOException {
        // Read entire file content
        String scriptContent = Files.readString(scriptPath);
        output.println("Executing script: " + scriptPath.getFileName());

        try {
            // Let the parser handle multiline commands and comments
            List<Instruction> instructions = parser.parse(scriptContent);
            app.executeCommand(instructions);
        } catch (Exception | LexerException e) {
            output.println("[Error] executing script: " + e.getMessage());
            e.printStackTrace(output);
        }
    }
}