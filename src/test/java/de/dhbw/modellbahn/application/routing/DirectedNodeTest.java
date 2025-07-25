package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.adapter.physical.railway.communication.ApiService;
import de.dhbw.modellbahn.adapter.physical.railway.communication.calls.TrackComponentCallsAdapter;
import de.dhbw.modellbahn.application.routing.directed.graph.DirectedNode;
import de.dhbw.modellbahn.domain.graph.nodes.attributes.PointName;
import de.dhbw.modellbahn.domain.graph.nodes.attributes.PointSide;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.graph.nodes.switches.NormalSwitch;
import de.dhbw.modellbahn.domain.physical.railway.communication.TrackComponentCalls;
import de.dhbw.modellbahn.domain.physical.railway.components.SwitchComponent;
import de.dhbw.modellbahn.domain.physical.railway.components.TrackComponentId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DirectedNodeTest {

    @Test
    void testEquals() {
        TrackComponentCalls componentCalls = new TrackComponentCallsAdapter(new ApiService(0));
        DirectedNode node1 = new DirectedNode(new NormalSwitch(new PointName("A"), new SwitchComponent(new TrackComponentId(0), componentCalls), new PointName("1"), new PointName("2"), new PointName("3")), PointSide.IN);
        DirectedNode node2 = new DirectedNode(GraphPoint.of("A"), PointSide.IN);
        DirectedNode node3 = new DirectedNode(GraphPoint.of("B"), PointSide.IN);

        assertEquals(node1, node2);
        assertNotEquals(node1, node3);
        assertNotEquals(node2, node3);
    }

}