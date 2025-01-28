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
import static org.junit.jupiter.api.Assertions.assertEquals;

class CrossSwitchTest {
    private static final String name = "Test";
    private static final GraphPoint root1 = new GraphPoint("root1"),
            root2 = new GraphPoint("root2"),
            turnout1 = new GraphPoint("turnout1"),
            turnout2 = new GraphPoint("turnout2");
    private static CrossSwitch testSwitch;

    @BeforeAll
    static void beforeAll() {
        ApiService apiService = new ApiService(0);
        TrackComponentCallsAdapter adapter = new TrackComponentCallsAdapter(apiService);
        SwitchComponent switchComponent = new SwitchComponent("switchComponent", new TrackComponentId(42), adapter);
        testSwitch = new CrossSwitch(name, switchComponent, root1, root2, turnout1, turnout2);
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
                Arguments.of(root1, new GraphPoint("Invalid"))
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
        assertEquals(SwitchSide.IN, testSwitch.getSwitchSideFromPoint(root1));
        assertEquals(SwitchSide.IN, testSwitch.getSwitchSideFromPoint(root2));
        assertEquals(SwitchSide.OUT, testSwitch.getSwitchSideFromPoint(turnout1));
        assertEquals(SwitchSide.OUT, testSwitch.getSwitchSideFromPoint(turnout2));
        assertEquals(SwitchSide.UNDEFINED, testSwitch.getSwitchSideFromPoint(new GraphPoint("unknown")));
    }
}