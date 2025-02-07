package de.dhbw.modellbahn.domain.graph;


import java.util.*;

public class Graph {
    private final Map<GraphPoint, List<WeightedEdge>> adjacencyList;

    public Graph() {
        this.adjacencyList = new HashMap<>();
    }

    public void addEdge(GraphPoint point1, GraphPoint point2, Distance distance) {
        this.adjacencyList.computeIfAbsent(point1, _ -> new ArrayList<>()).add(new WeightedEdge(point2, distance));
        this.adjacencyList.computeIfAbsent(point2, _ -> new ArrayList<>()).add(new WeightedEdge(point1, distance));
    }

    public void addEdge(GraphPoint startPoint, WeightedEdge weightedEdge) {
        this.addEdge(startPoint, weightedEdge.destination(), weightedEdge.distance());
    }

    public void addEdge(GraphConnection graphConnection) {
        this.addEdge(graphConnection.startPoint(), graphConnection.weightedEdge());
    }

    public List<WeightedEdge> getEdgesOfVertex(GraphPoint point) {
        return this.getEdgesOfVertex(point.getName());
    }

    public List<WeightedEdge> getEdgesOfVertex(String name) {
        List<WeightedEdge> returnList = new ArrayList<>();
        this.adjacencyList.keySet().forEach(vertex -> {
            if (vertex.getName().equals(name)) {
                returnList.addAll(this.adjacencyList.get(vertex));
            }
        });
        return returnList;
    }

    public Set<GraphPoint> getAllVertices() {
        return this.adjacencyList.keySet();
    }
}
