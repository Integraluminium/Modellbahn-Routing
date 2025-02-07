package de.dhbw.modellbahn.domain.graph;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {
    private static GraphPoint pointA, pointB, pointC;
    private static Distance distanceAB, distanceBC;
    private static final Height height = new Height(0);
    private static final boolean electrified = true;
    private static Graph testGraph;

    @BeforeAll
    static void beforeAll() {
        /*
        testGraph:  A ------ B ------ C
        distances:      10       20
         */
        pointA = new GraphPoint("A");
        pointB = new GraphPoint("B");
        pointC = new GraphPoint("C");

        distanceAB = new Distance(10);
        distanceBC = new Distance(20);

        testGraph = new Graph();
        testGraph.addEdge(pointA, pointB, distanceAB, height, electrified);
        testGraph.addEdge(pointB, new WeightedEdge(pointC, distanceBC, height, electrified));
    }

    @Test
    void testGetAllVertices() {
        Set<GraphPoint> actualPoints = testGraph.getAllVertices();
        Set<GraphPoint> expectedPoints = new HashSet<>();
        expectedPoints.add(pointA);
        expectedPoints.add(pointB);
        expectedPoints.add(pointC);

        assertEquals(actualPoints, expectedPoints);
    }

    @Test
    void testGetEdgesOfVertex() {
        List<WeightedEdge> actualEdges = testGraph.getEdgesOfVertex(pointB);
        WeightedEdge edgeBA = new WeightedEdge(pointA, distanceAB, height, electrified);
        WeightedEdge edgeBC = new WeightedEdge(pointC, distanceBC, height, electrified);
        List<WeightedEdge> expectedEdges = new ArrayList<>();
        expectedEdges.add(edgeBA);
        expectedEdges.add(edgeBC);

        assertEquals(actualEdges, expectedEdges);
    }
}