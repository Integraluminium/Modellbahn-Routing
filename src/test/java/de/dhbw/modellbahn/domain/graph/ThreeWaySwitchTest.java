package de.dhbw.modellbahn.domain.graph;

import de.dhbw.modellbahn.adapter.physical.railway.communication.ApiService;
import de.dhbw.modellbahn.adapter.physical.railway.communication.calls.TrackComponentCallsAdapter;
import de.dhbw.modellbahn.domain.graph.nodes.attributes.PointName;
import de.dhbw.modellbahn.domain.graph.nodes.attributes.PointSide;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.graph.nodes.switches.ThreeWaySwitch;
import de.dhbw.modellbahn.domain.physical.railway.components.SwitchComponent;
import de.dhbw.modellbahn.domain.physical.railway.components.TrackComponentId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ThreeWaySwitchTest {
    private static final PointName name = new PointName("Test");
    private static final GraphPoint root = GraphPoint.of("root"),
            straight = GraphPoint.of("straight"),
            left = GraphPoint.of("left"),
            right = GraphPoint.of("right");
    private static ThreeWaySwitch testSwitch;

    @BeforeAll
    static void beforeAll() {
        ApiService apiService = new ApiService(0);
        TrackComponentCallsAdapter adapter = new TrackComponentCallsAdapter(apiService);
        SwitchComponent switchComponent = new SwitchComponent(new TrackComponentId(42), adapter);
        SwitchComponent switchComponent2 = new SwitchComponent(new TrackComponentId(43), adapter);
        testSwitch = new ThreeWaySwitch(name, switchComponent, switchComponent2, root.getName(), straight.getName(), left.getName(), right.getName());
    }

    private static Stream<Arguments> provideValidConnectedPoints() {
        return Stream.of(
                Arguments.of(root, straight),
                Arguments.of(root, left),
                Arguments.of(root, right),
                Arguments.of(straight, root),
                Arguments.of(left, root),
                Arguments.of(right, root)
        );
    }

    private static Stream<Arguments> provideInvalidConnectedPoints() {
        return Stream.of(
                Arguments.of(root, root),
                Arguments.of(straight, left),
                Arguments.of(right, straight),
                Arguments.of(root, GraphPoint.of("Invalid"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidConnectedPoints")
    void testValidCheckIfConnectsPoints(GraphPoint point1, GraphPoint point2) {
        boolean connected = testSwitch.checkIfConnectsPoints(point1, point2);

        assertTrue(connected);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidConnectedPoints")
    void testInvalidCheckIfConnectsPoints(GraphPoint point1, GraphPoint point2) {
        boolean connected = testSwitch.checkIfConnectsPoints(point1, point2);

        assertFalse(connected);
    }

    @Test
    void testGetSwitchSideFromPoint() {
        assertEquals(PointSide.IN, testSwitch.getSwitchSideFromPoint(root));
        assertEquals(PointSide.OUT, testSwitch.getSwitchSideFromPoint(straight));
        assertEquals(PointSide.OUT, testSwitch.getSwitchSideFromPoint(left));
        assertEquals(PointSide.OUT, testSwitch.getSwitchSideFromPoint(right));
        assertThrows(IllegalArgumentException.class, () -> testSwitch.getSwitchSideFromPoint(GraphPoint.of("unknown")));
    }
}