package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.adapter.moba.communication.ApiService;
import de.dhbw.modellbahn.adapter.moba.communication.calls.TrackComponentCallsAdapter;
import de.dhbw.modellbahn.application.port.moba.communication.TrackComponentCalls;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.graph.NormalSwitch;
import de.dhbw.modellbahn.domain.graph.PointName;
import de.dhbw.modellbahn.domain.graph.PointSide;
import de.dhbw.modellbahn.domain.track_components.SwitchComponent;
import de.dhbw.modellbahn.domain.track_components.TrackComponentId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DirectedNodeTest {

    @Test
    void testEquals() {
        TrackComponentCalls componentCalls = new TrackComponentCallsAdapter(new ApiService(0));
        DirectedNode node1 = new DirectedNode(new NormalSwitch(new PointName("A"), new SwitchComponent(new TrackComponentId(0), componentCalls), new PointName("1"), new PointName("2"), new PointName("3")), PointSide.IN);
        DirectedNode node2 = new DirectedNode(new GraphPoint(new PointName("A")), PointSide.IN);
        DirectedNode node3 = new DirectedNode(new GraphPoint(new PointName("B")), PointSide.IN);

        assertEquals(node1, node2);
        assertNotEquals(node1, node3);
        assertNotEquals(node2, node3);
    }

}