package de.dhbw.modellbahn;

import de.dhbw.modellbahn.adapter.locomotive.LocomotiveRepositoryImpl;
import de.dhbw.modellbahn.adapter.moba.communication.ApiService;
import de.dhbw.modellbahn.adapter.moba.communication.calls.LocCallsAdapter;
import de.dhbw.modellbahn.adapter.moba.communication.calls.SystemCallsAdapter;
import de.dhbw.modellbahn.adapter.moba.communication.calls.TrackComponentCallsAdapter;
import de.dhbw.modellbahn.adapter.track.generation.GraphGenerator;
import de.dhbw.modellbahn.application.LocomotiveRepository;
import de.dhbw.modellbahn.application.RouteBuilder;
import de.dhbw.modellbahn.application.port.moba.communication.LocCalls;
import de.dhbw.modellbahn.application.port.moba.communication.SystemCalls;
import de.dhbw.modellbahn.application.port.moba.communication.TrackComponentCalls;
import de.dhbw.modellbahn.application.routing.PathNotPossibleException;
import de.dhbw.modellbahn.application.routing.Route;
import de.dhbw.modellbahn.domain.ConfigReader;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.plugin.YAMLConfigReader;
import de.dhbw.modellbahn.plugin.routing.jgrapht.RouteBuilderForJGraphT;

class RoutingApplication {
    private static final int SENDER_HASH = 25438; // just a random number - stolen from central station

    private final LocomotiveRepository locomotiveRepository;
    private final ConfigReader configReader;
    private final Graph domainGraph;

    public RoutingApplication() {
        configReader = new YAMLConfigReader();

        ApiService apiService = new ApiService(SENDER_HASH);
        LocCalls locCalls = new LocCallsAdapter(apiService);
        SystemCalls systemCalls = new SystemCallsAdapter(apiService);

        TrackComponentCalls trackComponentCalls = new TrackComponentCallsAdapter(apiService);
        domainGraph = new GraphGenerator(configReader, trackComponentCalls).generateGraph();
        locomotiveRepository = new LocomotiveRepositoryImpl(configReader, locCalls);
    }

    public static void main(String[] args) {
        RoutingApplication app = new RoutingApplication();
        try {
//            app.driveLocomotive("16389", "K2");
            app.driveLocomotive("16397", "W6");
        } catch (PathNotPossibleException e) {
            e.printStackTrace();
        }
    }

    public void driveLocomotive(String locomotiveId, String destinationName) throws PathNotPossibleException {
        int locomotiveIdInt = Integer.parseInt(locomotiveId);
        LocId locId = new LocId(locomotiveIdInt);
        Locomotive loc = locomotiveRepository.getLocomotive(locId);

        GraphPoint destination = GraphPoint.of(destinationName);
        RouteBuilder builder = new RouteBuilderForJGraphT(domainGraph).addLocomotive(loc).considerElectrification(false).setDestinationForLoc(loc, destination);

        builder.generateRoute();
        Route route = builder.getRouteForLoc(loc);

        route.driveRoute();
    }
}