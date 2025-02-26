package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.adapter.moba.communication.ApiService;
import de.dhbw.modellbahn.adapter.moba.communication.calls.TrackComponentCallsAdapter;
import de.dhbw.modellbahn.application.port.moba.communication.TrackComponentCalls;
import de.dhbw.modellbahn.domain.graph.Distance;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.graph.NormalSwitch;
import de.dhbw.modellbahn.domain.graph.PointName;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.domain.locomotive.MaxLocSpeed;
import de.dhbw.modellbahn.domain.locomotive.MockedLocomotive;
import de.dhbw.modellbahn.domain.track_components.SwitchComponent;
import de.dhbw.modellbahn.domain.track_components.TrackComponentId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

class RouteGeneratorTest {
    private static final MaxLocSpeed maxLocSpeed = new MaxLocSpeed(1.0);
    private static final long accelerationTime = 1000;
    private static final Distance accelerationDistance = new Distance(200);
    private static final Distance decelerationDistance = new Distance(200);
    private static final GraphPoint startPosition = new GraphPoint(new PointName("A"));
    private static final GraphPoint facingDirection = new GraphPoint(new PointName("B"));
    private static Locomotive loc;
    private static List<WeightedDistanceEdge> routingEdges;
    private static RouteGenerator routeGenerator;

    @BeforeAll
    static void beforeAll() {
        loc = new MockedLocomotive(maxLocSpeed, accelerationTime, accelerationDistance, decelerationDistance, startPosition, facingDirection);
        ApiService apiService = new ApiService(0);
        TrackComponentCalls trackComponentCalls = new TrackComponentCallsAdapter(apiService);
        SwitchComponent switchComponent = new SwitchComponent(new TrackComponentId(0), trackComponentCalls);
        routingEdges = List.of(
                new WeightedDistanceEdge(new GraphPoint(new PointName("A")), new Distance(0)),
                new WeightedDistanceEdge(new GraphPoint(new PointName("B")), new Distance(200)),
                new WeightedDistanceEdge(new GraphPoint(new PointName("C")), new Distance(400)),
                new WeightedDistanceEdge(new NormalSwitch(new PointName("D"), switchComponent, new PointName("C"), new PointName("A"), new PointName("B")), new Distance(300))

        );
        routeGenerator = new RouteGenerator(routingEdges, loc);
    }

    @Test
    void generateRoute() {
        try {
            Route route = routeGenerator.generateRoute();
        } catch (PathNotPossibleException e) {
            throw new RuntimeException(e);
        }
    }
}