package de.dhbw.modellbahn.plugin.parser;

import de.dhbw.modellbahn.adapter.locomotive.LocomotiveRepositoryImpl;
import de.dhbw.modellbahn.adapter.moba.communication.ApiService;
import de.dhbw.modellbahn.adapter.moba.communication.calls.LocCallsAdapter;
import de.dhbw.modellbahn.adapter.moba.communication.calls.SystemCallsAdapter;
import de.dhbw.modellbahn.adapter.moba.communication.calls.TrackComponentCallsAdapter;
import de.dhbw.modellbahn.adapter.track.generation.GraphGenerator;
import de.dhbw.modellbahn.application.LocomotiveRepository;
import de.dhbw.modellbahn.application.port.moba.communication.LocCalls;
import de.dhbw.modellbahn.application.port.moba.communication.SystemCalls;
import de.dhbw.modellbahn.application.port.moba.communication.TrackComponentCalls;
import de.dhbw.modellbahn.domain.ConfigReader;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.plugin.YAMLConfigReader;
import de.dhbw.modellbahn.plugin.parser.lexer.LexerException;
import de.dhbw.modellbahn.plugin.parser.lexer.instructions.Instruction;
import de.dhbw.modellbahn.util.MobaLogConfig;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ScriptRunner {
    private static final int SENDER_HASH = 25438;
    private static final Pattern INDENTATION_PATTERN = Pattern.compile("^\\s+");
    private static final Pattern COMMENT_PATTERN = Pattern.compile("^\\s*#.*");

    private final CommandParser parser;
    private final CommandExecutor executor;
    private final PrintStream output;

    public ScriptRunner(LocomotiveRepository repository, Graph graph, final SystemCalls systemCalls, PrintStream output) {
        this.parser = new CommandParser(new de.dhbw.modellbahn.plugin.parser.lexer.Lexer(), graph, repository);
        this.executor = new CommandExecutor(repository, graph, systemCalls, output);
        this.output = output;
    }

    public static void main(String[] args) {
        MobaLogConfig.configureLogging();
        if (args.length < 1) {
            System.err.println("Usage: ScriptRunner <script-file>");
            System.exit(1);
        }

        String scriptPath = args[0];

        // Initialize components similar to RoutingApplication
        ConfigReader configReader = new YAMLConfigReader();
        ApiService apiService = new ApiService(SENDER_HASH);
        LocCalls locCalls = new LocCallsAdapter(apiService);
        SystemCalls systemCalls = new SystemCallsAdapter(apiService);
        TrackComponentCalls trackComponentCalls = new TrackComponentCallsAdapter(apiService);

        Graph domainGraph = new GraphGenerator(configReader, trackComponentCalls).generateGraph();
        LocomotiveRepository locomotiveRepository = new LocomotiveRepositoryImpl(configReader, locCalls);

        // Create script runner and execute the script
        ScriptRunner scriptRunner = new ScriptRunner(locomotiveRepository, domainGraph, systemCalls, System.out);
        try {
            scriptRunner.runScript(scriptPath);
        } catch (IOException e) {
            System.err.println("Error reading script file: " + e.getMessage());
            System.exit(1);
        }
    }

    public void runScript(String scriptPath) throws IOException {
        Path path = Paths.get(scriptPath);
        List<String> lines = Files.readAllLines(path);

        int lineNumber = 0;
        List<String> commandBuffer = new ArrayList<>();
        boolean inMultilineCommand = false;
        boolean prevLineIndented = false;

        try {
            for (String line : lines) {
                lineNumber++;

                // Skip empty lines and comments
                if (line.trim().isEmpty() || COMMENT_PATTERN.matcher(line).matches()) {
                    continue;
                }

                boolean isIndented = INDENTATION_PATTERN.matcher(line).matches();

                // Start of a new command after previous command ended
                if (!isIndented && !inMultilineCommand && !commandBuffer.isEmpty()) {
                    executeCommand(String.join("\n", commandBuffer), lineNumber - commandBuffer.size());
                    commandBuffer.clear();
                }

                // Add current line to buffer
                commandBuffer.add(line);

                // Check if we're in a multiline command context
                inMultilineCommand = isIndented || line.endsWith("\\") ||
                        line.endsWith("{") || line.endsWith("(");

                prevLineIndented = isIndented;
            }

            // Process any remaining commands in buffer
            if (!commandBuffer.isEmpty()) {
                executeCommand(String.join("\n", commandBuffer), lineNumber - commandBuffer.size() + 1);
            }

        } catch (Exception e) {
            output.println("Error at line " + lineNumber + ": " + e.getMessage());
            e.printStackTrace(output);
        }
    }

    private void executeCommand(String command, int lineNumber) {
        try {
            output.println("Executing command at line " + lineNumber + ": " + command.replace("\n", " "));
            List<Instruction> instructions = parser.parse(command);
            executor.execute(instructions, true);
        } catch (Exception | LexerException e) {
            output.println("Error executing command at line " + lineNumber + ": " + e.getMessage());
        }
    }
}