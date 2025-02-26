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
import de.dhbw.modellbahn.domain.locomotive.Speed;
import de.dhbw.modellbahn.domain.track_components.SwitchComponent;
import de.dhbw.modellbahn.domain.track_components.TrackComponentId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


class RouteGeneratorTest {
    private static final MaxLocSpeed maxLocSpeed = new MaxLocSpeed(1.0);
    private static final long accelerationTime = 1000;
    private static final Distance accelerationDistance = new Distance(200);
    private static final Distance decelerationDistance = new Distance(200);
    private static final GraphPoint startPosition = new GraphPoint(new PointName("A"));
    private static final GraphPoint facingDirection = new GraphPoint(new PointName("B"));
    private static NormalSwitch normalSwitch;
    private static RouteGenerator routeGenerator;

    @BeforeAll
    static void beforeAll() {
        Locomotive loc = new MockedLocomotive(maxLocSpeed, accelerationTime, accelerationDistance, decelerationDistance, startPosition, facingDirection);
        ApiService apiService = new ApiService(0);
        TrackComponentCalls trackComponentCalls = new TrackComponentCallsAdapter(apiService);
        SwitchComponent switchComponent = new SwitchComponent(new TrackComponentId(0), trackComponentCalls);
        normalSwitch = new NormalSwitch(new PointName("D"), switchComponent, new PointName("C"), new PointName("A"), new PointName("B"));
        List<WeightedDistanceEdge> routingEdges = List.of(
                new WeightedDistanceEdge(new GraphPoint(new PointName("A")), new Distance(0)),
                new WeightedDistanceEdge(new GraphPoint(new PointName("B")), new Distance(200)),
                new WeightedDistanceEdge(new GraphPoint(new PointName("C")), new Distance(400)),
                new WeightedDistanceEdge(normalSwitch, new Distance(300))

        );
        routeGenerator = new RouteGenerator(routingEdges, loc);
    }

    @Test
    void generateRoute() {
        try {
            Route route = routeGenerator.generateRoute();
            List<RoutingAction> actualActionList = new ArrayList<>(route.getActionList());
            RoutingAction action1 = actualActionList.removeFirst();
            RoutingAction action2 = actualActionList.removeFirst();
            RoutingAction action3 = actualActionList.removeFirst();
            RoutingAction action4 = actualActionList.removeFirst();
            RoutingAction action5 = actualActionList.removeFirst();

            assertThat(action1).isExactlyInstanceOf(LocSpeedAction.class);
            assertEquals(((LocSpeedAction) action1).getLocSpeed(), new Speed(100));

            assertThat(action2).isExactlyInstanceOf(WaitAction.class);
            assertEquals(((WaitAction) action2).getWaitTime(), 1100);

            assertThat(action3).isExactlyInstanceOf(ChangeSwitchStateAction.class);
            assertEquals(((ChangeSwitchStateAction) action3).getPoint1(), new GraphPoint(new PointName("C")));
            assertEquals(((ChangeSwitchStateAction) action3).getPoint2(), normalSwitch.getPointThatCanConnectThisPoint(new GraphPoint(new PointName("C"))));

            assertThat(action4).isExactlyInstanceOf(WaitAction.class);
            assertEquals(((WaitAction) action4).getWaitTime(), 400);

            assertThat(action5).isExactlyInstanceOf(LocSpeedAction.class);
            assertEquals(((LocSpeedAction) action5).getLocSpeed(), new Speed(0));

        } catch (PathNotPossibleException e) {
            throw new RuntimeException(e);
        }
    }
}