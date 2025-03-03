package de.dhbw.modellbahn.domain.graph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeightedEdgeTest {
    @Test
    void testValidConstructor() {
        Distance distance = new Distance(10);
        Height height = new Height(0);
        boolean electrified = true;
        GraphPoint graphPoint = new GraphPoint(new PointName("Test"));

        WeightedEdge weightedEdge = new WeightedEdge(graphPoint, distance, height, electrified);

        assertEquals(graphPoint, weightedEdge.destination());
        assertEquals(distance, weightedEdge.distance());
    }

    @Test
    void testInvalidConstructor(){
        Distance distance = new Distance(10);
        Height height = new Height(0);
        boolean electrified = true;
        GraphPoint graphPoint = new GraphPoint(new PointName("Test"));

        assertThrows(IllegalArgumentException.class, () -> new WeightedEdge(null, distance, height, electrified));
        assertThrows(IllegalArgumentException.class, () -> new WeightedEdge(graphPoint, null, height, electrified));
        assertThrows(IllegalArgumentException.class, () -> new WeightedEdge(graphPoint, distance, null, electrified));
    }
}