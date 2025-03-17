package de.dhbw.modellbahn.domain.graph;


import java.util.*;

public class Graph {
    private final Map<GraphPoint, List<WeightedEdge>> adjacencyList;

    public Graph() {
        this.adjacencyList = new HashMap<>();
    }

    public void addEdge(GraphPoint point1, GraphPoint point2, Distance distance, Height height, boolean electrified) {
        this.adjacencyList.computeIfAbsent(point1, _ -> new ArrayList<>()).add(new WeightedEdge(point2, distance, height, electrified));
        if (!point1.equals(point2)) { // Allowing self loops without adding the edge twice
            this.adjacencyList.computeIfAbsent(point2, _ -> new ArrayList<>()).add(new WeightedEdge(point1, distance, height, electrified));
        }
    }

    public void addEdge(GraphPoint startPoint, WeightedEdge weightedEdge) {
        this.addEdge(startPoint, weightedEdge.destination(), weightedEdge.distance(), weightedEdge.height(), weightedEdge.electrified());
    }

    public void addEdge(GraphConnection graphConnection) {
        this.addEdge(graphConnection.startPoint(), graphConnection.weightedEdge());
    }

    public List<WeightedEdge> getEdgesOfVertex(GraphPoint point) {
        return this.adjacencyList.get(point);
    }

    public List<WeightedEdge> getEdgesOfVertex(PointName name) {
        GraphPoint point = new GraphPoint(name);
        return getEdgesOfVertex(point);
    }

    public Set<GraphPoint> getAllVertices() {
        return this.adjacencyList.keySet();
    }

    public boolean contains(GraphPoint point) {
        return this.adjacencyList.containsKey(point);
    }

    public List<GraphPoint> getNeighbors(GraphPoint point) {
        List<WeightedEdge> edgesOfVertex = getEdgesOfVertex(point);
        return edgesOfVertex.stream().map(WeightedEdge::destination).toList();
    }
}
