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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class CommandlineREPL {
    private static final String PROMPT = ">> ";
    private static final String CONTINUATION_PROMPT = "... ";
    private static final Pattern INDENTATION_PATTERN = Pattern.compile("^\\s+");
    private static final int SENDER_HASH = 25438; // same value used in RoutingApplication

    private final CommandParser parser;
    private final CommandExecutor executor;
    private final BufferedReader reader;
    private final PrintStream output;
    private boolean running = true;

    public CommandlineREPL(LocomotiveRepository repository, Graph graph, SystemCalls systemCalls, PrintStream output) {
        this.parser = new CommandParser(new de.dhbw.modellbahn.plugin.parser.lexer.Lexer(), graph, repository);
        this.executor = new CommandExecutor(repository, graph, systemCalls, output);
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.output = output;
    }

    public static void main(String[] args) {
        MobaLogConfig.configureLogging(Level.INFO);
        // Initialize components similar to RoutingApplication
        ConfigReader configReader = new YAMLConfigReader();

        ApiService apiService = new ApiService(SENDER_HASH);
        LocCalls locCalls = new LocCallsAdapter(apiService);
        SystemCalls systemCalls = new SystemCallsAdapter(apiService);
        TrackComponentCalls trackComponentCalls = new TrackComponentCallsAdapter(apiService);

        Graph domainGraph = new GraphGenerator(configReader, trackComponentCalls).generateGraph();
        LocomotiveRepository locomotiveRepository = new LocomotiveRepositoryImpl(configReader, locCalls);

        // Create and start REPL with the real components
        CommandlineREPL repl = new CommandlineREPL(locomotiveRepository, domainGraph, systemCalls, System.out);
        repl.start();
    }

    public void start() {
        printWelcome();

        while (running) {
            try {
                String command = readCommand();
                if (command.isEmpty()) continue;

                // Handle special commands
                if (handleSpecialCommand(command)) continue;

                // Parse and execute regular command
                List<Instruction> instructions = parser.parse(command);
                executor.execute(instructions, true);
            } catch (LexerException | ParseException e) {
                output.println("Parse error: " + e.getMessage());
            } catch (Exception e) {
                output.println("Execution error: " + e.getMessage());
                e.printStackTrace(output);
            }
        }
    }

    private String readCommand() throws IOException {
        output.print(PROMPT);
        output.flush();

        StringBuilder commandBuilder = new StringBuilder();
        String line = reader.readLine();
        if (line == null) {
            running = false;
            return "";
        }

        commandBuilder.append(line);
        boolean indented = hasIndentation(line);

        // Continue reading lines as long as they're indented
        while (true) {
            // If last line wasn't indented and current command isn't empty, stop collecting
            if (!indented && !commandBuilder.toString().trim().isEmpty()) {
                break;
            }

            // Check if continued input needed (indentation or special character at end)
            if (line != null && (indented || line.endsWith("\\") || line.endsWith("{") || line.endsWith("("))) {
                output.print(CONTINUATION_PROMPT);
                output.flush();

                line = reader.readLine();
                if (line == null) break;

                // Add new line to command
                if (commandBuilder.length() > 0) {
                    commandBuilder.append("\n");
                }
                commandBuilder.append(line);

                // Check if the new line is indented for next iteration
                indented = hasIndentation(line);
            } else {
                break;
            }
        }

        return commandBuilder.toString().trim();
    }

    private boolean hasIndentation(String line) {
        return INDENTATION_PATTERN.matcher(line).find();
    }

    private boolean handleSpecialCommand(String command) {
        command = command.trim().toLowerCase();

        if (command.equals("exit") || command.equals("quit")) {
            running = false;
            output.println("Exiting command interpreter.");
            return true;
        } else if (command.equals("help")) {
            printHelp();
            return true;
        }

        return false;
    }

    private void printWelcome() {
        output.println("Model Railway Command Interpreter");
        output.println("Type 'help' for available commands, 'exit' to quit");
        output.println();
    }

    private void printHelp() {
        output.println("Available commands:");
        output.println("  ADD <locId> [AT <position>] TO <destination> [FACING <direction>] [USING <optimization>] DRIVE [CONSIDER <considerations>]");
        output.println("  help - Display this help message");
        output.println("  exit/quit - Exit the command interpreter");
        output.println();
    }
}