package de.dhbw.modellbahn;

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
import de.dhbw.modellbahn.plugin.parser.CommandExecutor;
import de.dhbw.modellbahn.plugin.parser.lexer.instructions.Instruction;
import de.dhbw.modellbahn.util.MobaLogConfig;

import java.io.PrintStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoutingApplication {
    private static final Logger logger = Logger.getLogger(RoutingApplication.class.getName());
    private static final int SENDER_HASH = 25438;

    private final SystemCalls systemCalls;
    private final Graph domainGraph;
    private final LocomotiveRepository locomotiveRepository;

    private final CommandExecutor executor;

    public RoutingApplication() {
        this(Level.INFO);
    }

    public RoutingApplication(Level logLevel) {
        // Initialize logging
        MobaLogConfig.configureLogging(logLevel);

        logger.info("Initializing MobaRail application");

        // Initialize components
        ConfigReader configReader = new YAMLConfigReader();
        ApiService apiService = new ApiService(SENDER_HASH);
        LocCalls locCalls = new LocCallsAdapter(apiService);
        systemCalls = new SystemCallsAdapter(apiService);
        TrackComponentCalls trackComponentCalls = new TrackComponentCallsAdapter(apiService);

        logger.info("Generating track graph");
        domainGraph = new GraphGenerator(configReader, trackComponentCalls).generateGraph();
        locomotiveRepository = new LocomotiveRepositoryImpl(configReader, locCalls);

        // Initialize parser components
        executor = new CommandExecutor(locomotiveRepository, domainGraph, systemCalls, System.out);
    }

    // Getters for components (Java API)
    public Graph getGraph() {
        return domainGraph;
    }

    public LocomotiveRepository getLocomotiveRepository() {
        return locomotiveRepository;
    }

    public SystemCalls getSystemCalls() {
        return systemCalls;
    }

    public CommandExecutor getExecutor() {
        return executor;
    }

    public void executeCommand(List<Instruction> instructions, PrintStream output) throws Exception {
        CommandExecutor customExecutor = new CommandExecutor(
                locomotiveRepository, domainGraph, systemCalls, output);
        customExecutor.execute(instructions, true);
    }
}
