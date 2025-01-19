package de.dhbw.modellbahn.domain.graph;


import java.util.*;

public class Graph {
    private final Map<GraphPoint, List<WeightedEdge>> adjacencyList;

    public Graph() {
        this.adjacencyList = new HashMap<>();
    }

    public void addGraphPointConnection(GraphPoint point1, GraphPoint point2, Distance distance) {
        this.adjacencyList.computeIfAbsent(point1, _ -> new ArrayList<>()).add(new WeightedEdge(point2, distance));
        this.adjacencyList.computeIfAbsent(point2, _ -> new ArrayList<>()).add(new WeightedEdge(point1, distance));
    }

    public void addGraphPointConnection(GraphPoint startPoint, WeightedEdge weightedEdge) {
        this.addGraphPointConnection(startPoint, weightedEdge.destination(), weightedEdge.distance());
    }

    public List<WeightedEdge> getEdgesOfGraphPoint(GraphPoint point) {
        return this.adjacencyList.get(point);
    }

    public Set<GraphPoint> getAllGraphPoints() {
        return this.adjacencyList.keySet();
    }
}
