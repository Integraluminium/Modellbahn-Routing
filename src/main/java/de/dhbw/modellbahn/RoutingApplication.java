package de.dhbw.modellbahn;

import de.dhbw.modellbahn.adapter.physical.railway.communication.ApiService;
import de.dhbw.modellbahn.adapter.physical.railway.communication.calls.LocCallsAdapter;
import de.dhbw.modellbahn.adapter.physical.railway.communication.calls.SystemCallsAdapter;
import de.dhbw.modellbahn.adapter.physical.railway.communication.calls.TrackComponentCallsAdapter;
import de.dhbw.modellbahn.adapter.physical.railway.communication.socket.RailwayWebSocketClient;
import de.dhbw.modellbahn.adapter.track.generation.GraphGenerator;
import de.dhbw.modellbahn.application.ConfigReader;
import de.dhbw.modellbahn.application.repositories.LocomotiveRepository;
import de.dhbw.modellbahn.application.repositories.LocomotiveRepositoryImpl;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.physical.railway.communication.LocCalls;
import de.dhbw.modellbahn.domain.physical.railway.communication.SystemCalls;
import de.dhbw.modellbahn.domain.physical.railway.communication.TrackComponentCalls;
import de.dhbw.modellbahn.parser.CommandExecutor;
import de.dhbw.modellbahn.parser.instructions.Instruction;
import de.dhbw.modellbahn.plugin.YAMLConfigReader;
import de.dhbw.modellbahn.plugin.logging.MobaLogConfig;

import java.io.PrintStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoutingApplication {
    private static final Logger logger = Logger.getLogger(RoutingApplication.class.getName());
    private static final int SENDER_HASH = 25438;

    private final Graph domainGraph;
    private final LocomotiveRepository locomotiveRepository;
    private final CommandExecutor executor;
    private RailwayWebSocketClient webSocketClient;

    public RoutingApplication(PrintStream output) {
        this(Level.INFO, output);
    }

    public RoutingApplication(Level logLevel, PrintStream output) {
        // Initialize logging
        MobaLogConfig.configureLogging(logLevel);

        logger.info("Initializing MobaRail application");

        // Initialize components
        ConfigReader configReader = new YAMLConfigReader();
        ApiService apiService = new ApiService(SENDER_HASH);
        LocCalls locCalls = new LocCallsAdapter(apiService);
        SystemCalls systemCalls = new SystemCallsAdapter(apiService);
        TrackComponentCalls trackComponentCalls = new TrackComponentCallsAdapter(apiService);
        initializeWebSocket();

        logger.info("Generating track graph");
        domainGraph = new GraphGenerator(configReader, trackComponentCalls).generateGraph();
        locomotiveRepository = new LocomotiveRepositoryImpl(configReader, locCalls);

        // Initialize parser components
        executor = new CommandExecutor(locomotiveRepository, domainGraph, systemCalls, output);
    }

    // Getters for components (Java API)
    public Graph getGraph() {
        return domainGraph;
    }

    public LocomotiveRepository getLocomotiveRepository() {
        return locomotiveRepository;
    }

    public void executeCommand(List<Instruction> instructions) throws Exception {
        executor.execute(instructions, true);
    }

    // In your main application or service class:
    public void initializeWebSocket() {
        try {
            webSocketClient = new RailwayWebSocketClient("127.0.0.1", 8001);
            webSocketClient.connect().thenRun(() -> {
                if (webSocketClient.isConnected()) {
                    logger.info("WebSocket client connected and ready");
                }
            }).exceptionally(ex -> {
                logger.severe("WebSocket connection failed: " + ex.getMessage());
                return null;
            });
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error initializing WebSocket client", e);
        }

    }
}