package de.dhbw.modellbahn;

import de.dhbw.modellbahn.plugin.parser.*;
import de.dhbw.modellbahn.plugin.parser.lexer.Lexer;
import de.dhbw.modellbahn.util.MobaLogConfig;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

public class MobaControllApplication {
    public static void main(String[] args) {
        PrintStream standardOutput = System.out;
        RoutingApplication app = new RoutingApplication();
        DomainObjectParser domainObjectParser = new DomainObjectParser(app.getGraph(), app.getLocomotiveRepository());
        var executor = new CommandExecutor(app.getLocomotiveRepository(), app.getGraph(), app.getSystemCalls(), standardOutput);
        CommandParser parser = new CommandParser(new Lexer(), domainObjectParser);

        if (args.length == 0) {
            // Interactive REPL mode
            MobaLogConfig.configureLogging(Level.INFO);
            new CommandlineREPL(parser, executor, standardOutput).start();

        } else if (args[0].equals("--debug")) {
            // Set debug level and run REPL
            MobaLogConfig.configureLogging(Level.FINE);
            new CommandlineREPL(parser, executor, standardOutput).start();
            
        } else {
            // Script mode
            MobaLogConfig.configureLogging(Level.INFO);
            String scriptPath = args[0];
            Path path = Paths.get(scriptPath);
            if (!path.toFile().exists()) {
                System.err.println("Script file not found: " + scriptPath);
                System.exit(1);
            }
            try {
                new ScriptRunner(parser, executor, standardOutput).runScript(path);
            } catch (IOException e) {
                System.err.println("Error reading script: " + e.getMessage());
                System.exit(1);
            }
        }
    }
}
