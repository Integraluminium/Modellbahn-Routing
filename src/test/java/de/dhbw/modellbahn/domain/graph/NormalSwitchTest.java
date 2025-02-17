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
    private static final GraphPoint root = new GraphPoint(new PointName("root")),
            straight = new GraphPoint(new PointName("straight")),
            turnout = new GraphPoint(new PointName("turnout"));
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
                Arguments.of(root, new GraphPoint(new PointName("Invalid")))
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
        assertEquals(SwitchSide.IN, testSwitch.getSwitchSideFromPoint(root));
        assertEquals(SwitchSide.OUT, testSwitch.getSwitchSideFromPoint(straight));
        assertEquals(SwitchSide.OUT, testSwitch.getSwitchSideFromPoint(turnout));
        assertEquals(SwitchSide.UNDEFINED, testSwitch.getSwitchSideFromPoint(new GraphPoint(new PointName("unknown"))));
    }
}