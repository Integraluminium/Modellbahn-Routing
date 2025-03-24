package de.dhbw.modellbahn.domain.graph;

import de.dhbw.modellbahn.adapter.physical.railway.communication.ApiService;
import de.dhbw.modellbahn.adapter.physical.railway.communication.calls.TrackComponentCallsAdapter;
import de.dhbw.modellbahn.domain.graph.nodes.attributes.PointName;
import de.dhbw.modellbahn.domain.graph.nodes.attributes.PointSide;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.graph.nodes.switches.CrossSwitch;
import de.dhbw.modellbahn.domain.physical.railway.components.SwitchComponent;
import de.dhbw.modellbahn.domain.physical.railway.components.TrackComponentId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CrossSwitchTest {
    private static final PointName name = new PointName("Test");
    private static final GraphPoint root1 = GraphPoint.of("root1"),
            root2 = GraphPoint.of("root2"),
            turnout1 = GraphPoint.of("turnout1"),
            turnout2 = GraphPoint.of("turnout2");
    private static CrossSwitch testSwitch;

    @BeforeAll
    static void beforeAll() {
        ApiService apiService = new ApiService(0);
        TrackComponentCallsAdapter adapter = new TrackComponentCallsAdapter(apiService);
        SwitchComponent switchComponent = new SwitchComponent(new TrackComponentId(42), adapter);
        testSwitch = new CrossSwitch(name, switchComponent, root1.getName(), root2.getName(), turnout1.getName(), turnout2.getName());
    }

    private static Stream<Arguments> provideValidConnectedPoints() {
        return Stream.of(
                Arguments.of(root1, turnout1),
                Arguments.of(root1, turnout2),
                Arguments.of(root2, turnout1),
                Arguments.of(root2, turnout2)
        );
    }

    private static Stream<Arguments> provideInvalidConnectedPoints() {
        return Stream.of(
                Arguments.of(root1, root1),
                Arguments.of(root1, root2),
                Arguments.of(turnout1, turnout2),
                Arguments.of(root1, GraphPoint.of("Invalid"))
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
        assertEquals(PointSide.IN, testSwitch.getSwitchSideFromPoint(root1));
        assertEquals(PointSide.IN, testSwitch.getSwitchSideFromPoint(root2));
        assertEquals(PointSide.OUT, testSwitch.getSwitchSideFromPoint(turnout1));
        assertEquals(PointSide.OUT, testSwitch.getSwitchSideFromPoint(turnout2));
        assertThrows(IllegalArgumentException.class, () -> testSwitch.getSwitchSideFromPoint(GraphPoint.of("unknown")));
    }
}