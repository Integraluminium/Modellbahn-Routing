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
        testGraph.addGraphPointConnection(pointA, pointB, distanceAB);
        testGraph.addGraphPointConnection(pointB, new WeightedEdge(pointC, distanceBC));
    }

    @Test
    void testGetAllGraphPoints() {
        Set<GraphPoint> actualPoints = testGraph.getAllGraphPoints();
        Set<GraphPoint> expectedPoints = new HashSet<>();
        expectedPoints.add(pointA);
        expectedPoints.add(pointB);
        expectedPoints.add(pointC);

        assertEquals(actualPoints, expectedPoints);
    }

    @Test
    void testGetEdgesOfGraphPoint() {
        List<WeightedEdge> actualEdges = testGraph.getEdgesOfGraphPoint(pointB);
        WeightedEdge edgeBA = new WeightedEdge(pointA, distanceAB);
        WeightedEdge edgeBC = new WeightedEdge(pointC, distanceBC);
        List<WeightedEdge> expectedEdges = new ArrayList<>();
        expectedEdges.add(edgeBA);
        expectedEdges.add(edgeBC);

        assertEquals(actualEdges, expectedEdges);
    }
}