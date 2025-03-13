package de.dhbw.modellbahn.domain.graph;


import de.dhbw.modellbahn.adapter.moba.communication.ApiService;
import de.dhbw.modellbahn.adapter.moba.communication.calls.TrackComponentCallsAdapter;
import de.dhbw.modellbahn.domain.track_components.SwitchComponent;
import de.dhbw.modellbahn.domain.track_components.TrackComponentId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class NormalSwitchTest {
    private static final PointName name = new PointName("Test");
    private static final GraphPoint root = GraphPoint.of("root"),
            straight = GraphPoint.of("straight"),
            turnout = GraphPoint.of("turnout");
    private static NormalSwitch testSwitch;

    @BeforeAll
    static void beforeAll() {
        ApiService apiService = new ApiService(0);
        TrackComponentCallsAdapter adapter = new TrackComponentCallsAdapter(apiService);
        SwitchComponent switchComponent = new SwitchComponent(new TrackComponentId(42), adapter);
        testSwitch = new NormalSwitch(name, switchComponent, root.getName(), straight.getName(), turnout.getName());
    }

    private static Stream<Arguments> provideValidConnectedPoints() {
        return Stream.of(
                Arguments.of(root, straight),
                Arguments.of(root, turnout),
                Arguments.of(straight, root),
                Arguments.of(turnout, root)
        );
    }

    private static Stream<Arguments> provideInvalidConnectedPoints() {
        return Stream.of(
                Arguments.of(root, root),
                Arguments.of(straight, turnout),
                Arguments.of(turnout, straight),
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
        assertEquals(PointSide.OUT, testSwitch.getSwitchSideFromPoint(turnout));
        assertThrows(IllegalArgumentException.class, () -> testSwitch.getSwitchSideFromPoint(GraphPoint.of("unknown")));
    }
}