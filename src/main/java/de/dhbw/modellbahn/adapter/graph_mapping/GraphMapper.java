package de.dhbw.modellbahn.adapter.graph_mapping;

import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.graph.Switch;
import de.dhbw.modellbahn.domain.graph.WeightedEdge;

import java.util.List;
import java.util.Set;

public abstract class GraphMapper {

    public static DirectionalEdgeGraph mapToOneDirectionalGraph(Graph originalGraph) {
        DirectionalEdgeGraph newGraph = new DirectionalEdgeGraph();

        Set<GraphPoint> graphPoints = originalGraph.getAllVertices();
        graphPoints.forEach(vertex -> {
            List<WeightedEdge> adjacentEdges = originalGraph.getEdgesOfVertex(vertex);
            addEdgesToNewGraph(newGraph, vertex, adjacentEdges);
        });

        return newGraph;
    }

    private static void addEdgesToNewGraph(DirectionalEdgeGraph newGraph, GraphPoint vertex, List<WeightedEdge> adjacentEdges) {
        adjacentEdges.forEach(adjacentEdge -> {
            addAdjacentEdgeToNewGraph(newGraph, new DirectionalEdge(vertex, adjacentEdge.destination(), adjacentEdge.distance()));
        });
    }

    private static void addAdjacentEdgeToNewGraph(DirectionalEdgeGraph newGraph, DirectionalEdge adjacentEdge) {
        addVerticesToNewGraph(newGraph, adjacentEdge);

        Set<DirectionalEdge> allNewVertices = newGraph.getAllVertices();
        allNewVertices.forEach(vertex -> {
            // Only add edge to vertex of new graph if there's not already an edge
            if (vertex.destinationPoint() == adjacentEdge.startPoint() && vertex.startPoint() != adjacentEdge.destinationPoint()) {
                if (checkIfTargetPointIsSwitchAndConnectsPoints(vertex.destinationPoint(), vertex.startPoint(), adjacentEdge.destinationPoint())) {
                    newGraph.addEdge(vertex, adjacentEdge);
                }
            } else if (vertex.startPoint() == adjacentEdge.destinationPoint() && vertex.destinationPoint() != adjacentEdge.startPoint())
                if (checkIfTargetPointIsSwitchAndConnectsPoints(vertex.startPoint(), vertex.destinationPoint(), adjacentEdge.startPoint())) {
                    newGraph.addEdge(adjacentEdge, vertex);
                }
        });
    }

    private static boolean checkIfTargetPointIsSwitchAndConnectsPoints(GraphPoint targetPoint, GraphPoint point1, GraphPoint point2) {
        if (targetPoint instanceof Switch) {
            return ((Switch) targetPoint).checkIfConnectsPoints(point1, point2);
        }
        return true;
    }

    private static void addVerticesToNewGraph(DirectionalEdgeGraph newGraph, DirectionalEdge directionalEdge) {
        DirectionalEdge reversedEdge = new DirectionalEdge(directionalEdge.destinationPoint(), directionalEdge.startPoint(), directionalEdge.distance());
        newGraph.addVertex(directionalEdge);
        newGraph.addVertex(reversedEdge);
    }
}
