package de.dhbw.modellbahn.domain.graph;

import de.dhbw.modellbahn.domain.graph.nodes.attributes.PointName;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphPointTest {
    @Test
    void testInvalidConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new GraphPoint(null));
        assertThrows(IllegalArgumentException.class, () -> new GraphPoint(new PointName("")));
    }

    @Test
    void testGetName() {
        PointName name = new PointName("Test");
        GraphPoint graphPoint = new GraphPoint(name);

        assertEquals(name, graphPoint.getName());
    }

    @Test
    void testEquals() {
        PointName name = new PointName("Test");
        GraphPoint point1 = new GraphPoint(name);
        GraphPoint point2 = new GraphPoint(name);

        assertEquals(point1, point2);
    }

    @Test
    void testEqualsWithString() {
        PointName name = new PointName("Test");
        GraphPoint point1 = new GraphPoint(name);

        assertTrue(point1.equals(name));
    }
}