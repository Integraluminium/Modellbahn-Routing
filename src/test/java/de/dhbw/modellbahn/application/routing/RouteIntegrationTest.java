package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.adapter.moba.communication.ApiService;
import de.dhbw.modellbahn.adapter.moba.communication.calls.LocCallsAdapter;
import de.dhbw.modellbahn.adapter.moba.communication.calls.MockedLockCalls;
import de.dhbw.modellbahn.adapter.moba.communication.calls.SystemCallsAdapter;
import de.dhbw.modellbahn.adapter.moba.communication.calls.TrackComponentCallsAdapter;
import de.dhbw.modellbahn.application.port.moba.communication.LocCalls;
import de.dhbw.modellbahn.application.port.moba.communication.SystemCalls;
import de.dhbw.modellbahn.application.port.moba.communication.TrackComponentCalls;
import de.dhbw.modellbahn.domain.ConfigReader;
import de.dhbw.modellbahn.domain.graph.Distance;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.graph.NormalSwitch;
import de.dhbw.modellbahn.domain.graph.PointName;
import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.domain.locomotive.LocName;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.domain.locomotive.MaxLocSpeed;
import de.dhbw.modellbahn.domain.track_components.SwitchComponent;
import de.dhbw.modellbahn.domain.track_components.TrackComponentId;
import de.dhbw.modellbahn.plugin.YAMLConfigReader;

import java.util.ArrayList;
import java.util.List;

class RouteIntegrationTest {
    private ConfigReader configReader;

    private LocCalls locCalls;
    private SystemCalls systemCalls;
    private TrackComponentCalls trackComponentCalls;

    public static void main(String[] args) {
        try {
            RouteIntegrationTest test = new RouteIntegrationTest();
            test.setUp();
            test.testSomething();
        } catch (PathNotPossibleException e) {
            e.printStackTrace();
        }

    }

    void setUp() {
        configReader = new YAMLConfigReader();

        ApiService apiService = new ApiService(25438); // just a random number - stolen from central station

        locCalls = new LocCallsAdapter(apiService);
        systemCalls = new SystemCallsAdapter(apiService);
        trackComponentCalls = new TrackComponentCallsAdapter(apiService);
    }

    void testSomething() throws PathNotPossibleException {
        System.out.println("Starting test");

        NormalSwitch switchComponent2 = createSwitchComponent2(12291, "A", createPointName("B"), createPointName("B"), createPointName("C"));
        NormalSwitch switchComponent3 = createSwitchComponent2(12298, "B", createPointName("C"), createPointName("C"), createPointName("A"));
        NormalSwitch switchComponent1 = createSwitchComponent2(12298, "C", createPointName("A"), createPointName("A"), createPointName("B"));

        Locomotive mockedLocomotive = getMockedLocomotive(createGraphPoint("A"), createGraphPoint("B"));

        WeightedDistanceEdge switchEdge1 = new WeightedDistanceEdge(switchComponent2, new Distance(3));
        WeightedDistanceEdge switchEdge2 = new WeightedDistanceEdge(switchComponent3, new Distance(3));
        WeightedDistanceEdge switchEdge3 = new WeightedDistanceEdge(switchComponent1, new Distance(3));

        List<WeightedDistanceEdge> routingEdges = new ArrayList<>();
        routingEdges.add(switchEdge1);
        routingEdges.add(switchEdge2);
        routingEdges.add(switchEdge3);

        RouteGenerator routeGenerator = new RouteGenerator(mockedLocomotive, routingEdges, createGraphPoint("B"));

        System.out.println("Generating route");
        Route route = routeGenerator.generateRoute();
        System.out.println("Driving route");
        route.driveRoute();
        System.out.println("Route driven");
    }

    private NormalSwitch createSwitchComponent2(int id, String name, PointName root, PointName straight, PointName turnout) {
        SwitchComponent switchComponent = new SwitchComponent(new TrackComponentId(id), trackComponentCalls);
        return new NormalSwitch(createPointName(name), switchComponent, root, straight, turnout);
    }

    private Locomotive getMockedLocomotive(GraphPoint start, GraphPoint facingDirection) {
        LocName name = new LocName("TestLoc");
        LocId locId = new LocId(16389, configReader);
        LocCalls locCalls = new MockedLockCalls();
        return new Locomotive(name, locId, new MaxLocSpeed(50), 0, new Distance(0), new Distance(0), start, facingDirection, locCalls);
    }

    private PointName createPointName(String name) {
        return new PointName(name);
    }

    private GraphPoint createGraphPoint(String name) {
        return new GraphPoint(createPointName(name));
    }
}