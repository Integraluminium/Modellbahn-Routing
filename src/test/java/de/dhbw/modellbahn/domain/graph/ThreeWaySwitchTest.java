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

class ThreeWaySwitchTest {
    private static final String name = "Test";
    private static final GraphPoint root = new GraphPoint("root"),
            straight = new GraphPoint("straight"),
            left = new GraphPoint("left"),
            right = new GraphPoint("right");
    private static ThreeWaySwitch testSwitch;

    @BeforeAll
    static void beforeAll() {
        ApiService apiService = new ApiService(0);
        TrackComponentCallsAdapter adapter = new TrackComponentCallsAdapter(apiService);
        SwitchComponent switchComponent = new SwitchComponent("switchComponent", new TrackComponentId(42), adapter);
        SwitchComponent switchComponent2 = new SwitchComponent("switchComponent2", new TrackComponentId(43), adapter);
        testSwitch = new ThreeWaySwitch(name, switchComponent, switchComponent2, root, straight, left, right);
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
                Arguments.of(root, new GraphPoint("Invalid"))
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
        assertThrows(IllegalArgumentException.class, () -> testSwitch.getSwitchSideFromPoint(new GraphPoint("unknown")));
    }
}