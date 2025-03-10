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
                if (point.equals(edge.destination())) {
                    // Special case if a switch is connected to itself
                    DefaultWeightedEdge newEdge = newGraph.addEdge(new DirectedNode(point, sourceSide.getOpposite()), new DirectedNode(point, sourceSide));
                    newGraph.setEdgeWeight(newEdge, edge.distance().value());
                } else {
                    addEdge(newGraph, point, sourceSide, edge);
                }
            }
        } else {
            PointSide sourceSide = PointSide.IN;
            if (adjacentEdges.size() > 2) {
                throw new IllegalArgumentException("A non-switch point can only have a maximum of 2 adjacent edges.");
            }
            if (containsGraphPoint(newGraph, point)) {
                if (outSideAlreadyConnected(newGraph, point)) {
                    return;
                }
                for (WeightedEdge edge : adjacentEdges) {
                    Set<DefaultWeightedEdge> edges = newGraph.edgesOf(new DirectedNode(point, PointSide.IN));
                    System.out.println(edges);
                    if (isEdgeAlreadyConnected(newGraph, edge, edges)) {
                        continue;
                    }
                    addEdge(newGraph, point, PointSide.OUT, edge);
                }
            } else {
                for (WeightedEdge edge : adjacentEdges) {
                    addEdge(newGraph, point, sourceSide, edge);
                    sourceSide = sourceSide.getOpposite();
                }
            }
        }
    }

    private boolean isEdgeAlreadyConnected(final org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> newGraph, final WeightedEdge edge, final Set<DefaultWeightedEdge> edges) {
        for (DefaultWeightedEdge weightedEdge : edges) {
            if (newGraph.getEdgeTarget(weightedEdge).getPoint().equals(edge.destination()) || newGraph.getEdgeSource(weightedEdge).getPoint().equals(edge.destination())) {
                return true;
            }
        }
        return false;
    }

    private void addEdge(org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> newGraph, GraphPoint point, PointSide sourceSide, WeightedEdge edge) {
        PointSide destinationSide;
        if (edge.destination() instanceof Switch) {
            destinationSide = ((Switch) edge.destination()).getSwitchSideFromPoint(point);
            newGraph.addVertex(new DirectedNode(edge.destination(), PointSide.IN));
            newGraph.addVertex(new DirectedNode(edge.destination(), PointSide.OUT));
        } else {
            if (containsGraphPoint(newGraph, edge.destination())) {
                if (outSideAlreadyConnected(newGraph, edge.destination())) {
                    return;
                }
                destinationSide = PointSide.OUT;
            } else {
                destinationSide = PointSide.IN;
                newGraph.addVertex(new DirectedNode(edge.destination(), PointSide.IN));
                newGraph.addVertex(new DirectedNode(edge.destination(), PointSide.OUT));
            }
        }
        DefaultWeightedEdge newEdge1 = newGraph.addEdge(new DirectedNode(point, sourceSide.getOpposite()), new DirectedNode(edge.destination(), destinationSide));
        DefaultWeightedEdge newEdge2 = newGraph.addEdge(new DirectedNode(edge.destination(), destinationSide.getOpposite()), new DirectedNode(point, sourceSide));

        // Only set weight if edge does not already exist
        if (newEdge1 != null) {
            newGraph.setEdgeWeight(newEdge1, edge.distance().value());
        }
        if (newEdge2 != null) {
            newGraph.setEdgeWeight(newEdge2, edge.distance().value());
        }
    }

    private boolean outSideAlreadyConnected(org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> newGraph, GraphPoint point) {
        return newGraph.edgesOf(new DirectedNode(point, PointSide.OUT)).size() > 1;
    }

    private boolean containsGraphPoint(org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> newGraph, GraphPoint point) {
        return newGraph.vertexSet().stream().anyMatch(node -> node.getPoint().equals(point));
    }
}
