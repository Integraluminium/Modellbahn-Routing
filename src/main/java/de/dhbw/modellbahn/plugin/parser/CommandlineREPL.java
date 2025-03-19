package de.dhbw.modellbahn.plugin.parser;

import de.dhbw.modellbahn.plugin.parser.lexer.LexerException;
import de.dhbw.modellbahn.plugin.parser.lexer.instructions.Instruction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;
import java.util.regex.Pattern;

public class CommandlineREPL {
    private static final String PROMPT = ">> ";
    private static final String CONTINUATION_PROMPT = "... ";
    private static final Pattern INDENTATION_PATTERN = Pattern.compile("^\\s+");

    private final CommandParser parser;
    private final CommandExecutor executor;
    private final BufferedReader reader;
    private final PrintStream output;
    private boolean running = true;

    public CommandlineREPL(CommandParser parser, CommandExecutor executor, PrintStream output) {
        this.parser = parser;
        this.executor = executor;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.output = output;
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