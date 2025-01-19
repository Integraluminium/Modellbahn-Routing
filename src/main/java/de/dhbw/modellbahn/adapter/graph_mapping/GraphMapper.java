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

        Set<GraphPoint> graphPoints = originalGraph.getAllGraphPoints();
        graphPoints.forEach(point -> {
            List<WeightedEdge> adjacentEdges = originalGraph.getEdgesOfGraphPoint(point);
            addEdgesToGraph(newGraph, point, adjacentEdges);
        });

        return newGraph;
    }

    private static void addEdgesToGraph(DirectionalEdgeGraph newGraph, GraphPoint point, List<WeightedEdge> adjacentEdges) {
        adjacentEdges.forEach(weightedEdge -> {
            addAdjacentEdgeConnections(newGraph, new DirectionalEdge(point, weightedEdge.destination(), weightedEdge.distance()));
        });
    }

    private static void addAdjacentEdgeConnections(DirectionalEdgeGraph newGraph, DirectionalEdge directionalEdge) {
        addEdgesToGraph(newGraph, directionalEdge);

        Set<DirectionalEdge> allEdges = newGraph.getAllGraphEdges();
        allEdges.forEach(edge -> {
            if (edge.destinationPoint() == directionalEdge.startPoint() && edge.startPoint() != directionalEdge.destinationPoint()) {
                if (checkIfTargetPointIsSwitchAndConnectsPoints(edge.destinationPoint(), edge.startPoint(), directionalEdge.destinationPoint())) {
                    newGraph.addGraphEdgeConnection(edge, directionalEdge);
                }
            } else if (edge.startPoint() == directionalEdge.destinationPoint() && edge.destinationPoint() != directionalEdge.startPoint())
                if (checkIfTargetPointIsSwitchAndConnectsPoints(edge.startPoint(), edge.destinationPoint(), directionalEdge.startPoint())) {
                    newGraph.addGraphEdgeConnection(directionalEdge, edge);
                }
        });
    }

    private static boolean checkIfTargetPointIsSwitchAndConnectsPoints(GraphPoint targetPoint, GraphPoint point1, GraphPoint point2) {
        if (targetPoint instanceof Switch) {
            return ((Switch) targetPoint).checkIfSwitchConnectsPoints(point1, point2);
        }
        return true;
    }

    private static void addEdgesToGraph(DirectionalEdgeGraph newGraph, DirectionalEdge directionalEdge) {
        DirectionalEdge reversedEdge = new DirectionalEdge(directionalEdge.destinationPoint(), directionalEdge.startPoint(), directionalEdge.distance());
        newGraph.addGraphEdge(directionalEdge);
        newGraph.addGraphEdge(reversedEdge);
    }
}
