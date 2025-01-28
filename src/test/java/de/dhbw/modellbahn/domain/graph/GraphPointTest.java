package de.dhbw.modellbahn.domain.graph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphPointTest {
    @Test
    void testInvalidConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new GraphPoint(null));
        assertThrows(IllegalArgumentException.class, () -> new GraphPoint(""));
    }

    @Test
    void testGetName() {
        String name = "Test";
        GraphPoint graphPoint = new GraphPoint(name);

        assertEquals(name, graphPoint.getName());
    }

    @Test
    void testEquals() {
        String name = "Test";
        GraphPoint point1 = new GraphPoint(name);
        GraphPoint point2 = new GraphPoint(name);

        assertEquals(point1, point2);
    }
}