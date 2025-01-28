package de.dhbw.modellbahn.domain.graph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeightedEdgeTest {
    @Test
    void testValidConstructor() {
        Distance distance = new Distance(10);
        GraphPoint graphPoint = new GraphPoint("Test");

        WeightedEdge weightedEdge = new WeightedEdge(graphPoint, distance);

        assertEquals(graphPoint, weightedEdge.destination());
        assertEquals(distance, weightedEdge.distance());
    }

    @Test
    void testInvalidConstructor(){
        Distance distance = new Distance(10);
        GraphPoint graphPoint = new GraphPoint("Test");

        assertThrows(IllegalArgumentException.class, () -> new WeightedEdge(null, distance));
        assertThrows(IllegalArgumentException.class, () -> new WeightedEdge(graphPoint, null));
    }
}