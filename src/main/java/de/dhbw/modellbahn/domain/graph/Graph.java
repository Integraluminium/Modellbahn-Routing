package de.dhbw.modellbahn.domain.graph;


import de.dhbw.modellbahn.domain.graph.edges.GraphConnection;
import de.dhbw.modellbahn.domain.graph.edges.WeightedEdge;
import de.dhbw.modellbahn.domain.graph.nodes.attributes.Distance;
import de.dhbw.modellbahn.domain.graph.nodes.attributes.Height;
import de.dhbw.modellbahn.domain.graph.nodes.attributes.PointName;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;

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
        return new ArrayList<>(this.adjacencyList.get(point));
    }

    public List<WeightedEdge> getEdgesOfVertex(PointName name) {
        GraphPoint point = new GraphPoint(name);
        return getEdgesOfVertex(point);
    }

    public Set<GraphPoint> getAllVertices() {
        return new HashSet<>(this.adjacencyList.keySet());
    }

    public boolean contains(GraphPoint point) {
        return this.adjacencyList.containsKey(point);
    }

    public GraphPoint getGraphPoint(PointName name) {
        return this.adjacencyList.keySet().stream()
                .filter(point -> point.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("GraphPoint with name " + name + " is not in the graph."));
    }

    public List<GraphPoint> getNeighbors(GraphPoint point) {
        List<WeightedEdge> edgesOfVertex = getEdgesOfVertex(point);
        return edgesOfVertex.stream().map(WeightedEdge::destination).toList();
    }
}
