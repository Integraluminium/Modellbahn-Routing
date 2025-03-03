package de.dhbw.modellbahn.adapter.graph_mapping;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class DirectionalEdgeGraph {
    private final Map<DirectionalEdge, Set<DirectionalEdge>> adjacencyList;

    public DirectionalEdgeGraph() {
        this.adjacencyList = new HashMap<>();
    }

    public void addEdge(DirectionalEdge sourceEdge, DirectionalEdge destinationEdge) {
        this.adjacencyList.computeIfAbsent(sourceEdge, _ -> new HashSet<>()).add(destinationEdge);
    }

    public void addVertex(DirectionalEdge edge) {
        this.adjacencyList.putIfAbsent(edge, new HashSet<>());
    }

    public Set<DirectionalEdge> getEdgesOfVertex(DirectionalEdge edge) {
        return this.adjacencyList.get(edge);
    }

    public Set<DirectionalEdge> getAllVertices() {
        return this.adjacencyList.keySet();
    }
}

