package de.dhbw.modellbahn.adapter.routing;

import de.dhbw.modellbahn.application.routing.DirectedNode;
import de.dhbw.modellbahn.domain.graph.*;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;
import java.util.Set;

public class GraphMapper {
    public org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> mapGraph(Graph originalGraph) {
        org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> newGraph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        Set<GraphPoint> graphPoints = originalGraph.getAllVertices();
        graphPoints.forEach(vertex -> {
            List<WeightedEdge> adjacentEdges = originalGraph.getEdgesOfVertex(vertex);
            createVertices(newGraph, vertex, adjacentEdges);
        });

        return newGraph;
    }

    private void createVertices(org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> newGraph, GraphPoint point, List<WeightedEdge> adjacentEdges) {
        newGraph.addVertex(new DirectedNode(point, PointSide.IN));
        newGraph.addVertex(new DirectedNode(point, PointSide.OUT));
        if (point instanceof Switch) {
            for (WeightedEdge edge : adjacentEdges) {
                PointSide sourceSide = ((Switch) point).getSwitchSideFromPoint(edge.destination());
                addEdge(newGraph, point, sourceSide, edge);
            }
        } else {
            PointSide sourceSide = PointSide.IN;
            if (adjacentEdges.size() > 2) {
                throw new IllegalArgumentException("A non-switch point can only have a maximum of 2 adjacent edges.");
            }
            for (WeightedEdge edge : adjacentEdges) {
                addEdge(newGraph, point, sourceSide, edge);
                sourceSide = sourceSide.getOpposite();
            }
        }
    }

    private void addEdge(org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> newGraph, GraphPoint point, PointSide sourceSide, WeightedEdge edge) {
        PointSide destinationSide;
        if (edge.destination() instanceof Switch) {
            destinationSide = ((Switch) edge.destination()).getSwitchSideFromPoint(point);
        } else {
            if (containsGraphPoint(newGraph, edge.destination())) {
                if (outSideAlreadyConnected(newGraph, edge.destination())) {
                    return;
                }
                destinationSide = PointSide.OUT;
            } else {
                newGraph.addVertex(new DirectedNode(edge.destination(), PointSide.IN));
                newGraph.addVertex(new DirectedNode(edge.destination(), PointSide.OUT));
                destinationSide = PointSide.IN;
            }
        }
        newGraph.addEdge(new DirectedNode(point, sourceSide.getOpposite()), new DirectedNode(edge.destination(), destinationSide));
        newGraph.addEdge(new DirectedNode(edge.destination(), destinationSide.getOpposite()), new DirectedNode(point, sourceSide));
    }

    private boolean outSideAlreadyConnected(org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> newGraph, GraphPoint point) {
        return newGraph.edgesOf(new DirectedNode(point, PointSide.OUT)).size() > 0;
    }

    private boolean containsGraphPoint(org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> newGraph, GraphPoint point) {
        return newGraph.vertexSet().stream().anyMatch(node -> node.getPoint().equals(point));
    }
}
