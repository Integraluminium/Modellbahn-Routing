package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.adapter.physical.railway.communication.ApiService;
import de.dhbw.modellbahn.adapter.physical.railway.communication.calls.TrackComponentCallsAdapter;
import de.dhbw.modellbahn.application.routing.actions.ChangeSwitchStateAction;
import de.dhbw.modellbahn.application.routing.actions.LocSpeedAction;
import de.dhbw.modellbahn.application.routing.actions.RoutingAction;
import de.dhbw.modellbahn.application.routing.actions.WaitAction;
import de.dhbw.modellbahn.application.routing.building.PathNotPossibleException;
import de.dhbw.modellbahn.application.routing.directed.graph.WeightedDistanceEdge;
import de.dhbw.modellbahn.domain.graph.nodes.attributes.Distance;
import de.dhbw.modellbahn.domain.graph.nodes.attributes.PointName;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.graph.nodes.switches.NormalSwitch;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.domain.locomotive.MockedLocomotive;
import de.dhbw.modellbahn.domain.locomotive.attributes.MaxLocSpeed;
import de.dhbw.modellbahn.domain.locomotive.attributes.Speed;
import de.dhbw.modellbahn.domain.physical.railway.communication.TrackComponentCalls;
import de.dhbw.modellbahn.domain.physical.railway.components.SwitchComponent;
import de.dhbw.modellbahn.domain.physical.railway.components.TrackComponentId;
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
    private static final long decelerationTime = 1000;
    private static final Distance decelerationDistance = new Distance(200);
    private static final GraphPoint startPosition = GraphPoint.of("A");
    private static final GraphPoint facingDirection = GraphPoint.of("B");
    private static final GraphPoint point1 = GraphPoint.of("A");
    private static final GraphPoint point2 = GraphPoint.of("B");
    private static final GraphPoint point3 = GraphPoint.of("C");
    private static GraphPoint newFacingDirection;
    private static RouteGenerator routeGenerator;

    @BeforeAll
    static void beforeAll() {
        Locomotive loc = new MockedLocomotive(maxLocSpeed, accelerationTime, accelerationDistance, decelerationTime, decelerationDistance, startPosition, facingDirection);
        ApiService apiService = new ApiService(0);
        TrackComponentCalls trackComponentCalls = new TrackComponentCallsAdapter(apiService);
        SwitchComponent switchComponent = new SwitchComponent(new TrackComponentId(0), trackComponentCalls);
        NormalSwitch normalSwitch = new NormalSwitch(new PointName("D"), switchComponent, point3.getName(), point1.getName(), point2.getName());
        newFacingDirection = normalSwitch.getPointThatCanConnectThisPoint(point3);
        List<WeightedDistanceEdge> routingEdges = List.of(
                new WeightedDistanceEdge(point1, new Distance(0)),
                new WeightedDistanceEdge(point2, new Distance(200)),
                new WeightedDistanceEdge(point3, new Distance(400)),
                new WeightedDistanceEdge(normalSwitch, new Distance(300))

        );
        routeGenerator = new RouteGenerator(loc, routingEdges, newFacingDirection);
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
            assertEquals(((ChangeSwitchStateAction) action3).getPoint1(), point3);
            assertEquals(((ChangeSwitchStateAction) action3).getPoint2(), newFacingDirection);

            assertThat(action4).isExactlyInstanceOf(WaitAction.class);
            assertEquals(((WaitAction) action4).getWaitTime(), 400);

            assertThat(action5).isExactlyInstanceOf(LocSpeedAction.class);
            assertEquals(((LocSpeedAction) action5).getLocSpeed(), new Speed(0));

            assertEquals(route.getEstimatedTime(), 2500);
        } catch (PathNotPossibleException e) {
            throw new RuntimeException(e);
        }
    }
}