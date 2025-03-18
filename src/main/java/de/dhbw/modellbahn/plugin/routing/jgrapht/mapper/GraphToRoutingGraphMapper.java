package de.dhbw.modellbahn.plugin.routing.jgrapht.mapper;

import de.dhbw.modellbahn.application.routing.DirectedNode;
import de.dhbw.modellbahn.domain.graph.*;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GraphToRoutingGraphMapper {
    private static void addEdgeToGraph(final org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> graph, final DirectedNode source, final DirectedNode target, double weight) {
        DefaultWeightedEdge edge = graph.addEdge(source, target);
        if (edge != null) {
            graph.setEdgeWeight(edge, weight);
        }
    }


    public org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> mapGraphToJGraphT(Graph originalGraph) {
        return mapGraphToJGraphT(originalGraph, false, new ArrayList<>());
    }

    public org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> mapGraphToJGraphT(Graph originalGraph, boolean electrified, List<GraphPoint> blockedPoints) {
        Objects.requireNonNull(originalGraph);
        Objects.requireNonNull(blockedPoints);

        org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> newGraph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        Set<GraphPoint> graphPoints = originalGraph.getAllVertices();
        if (!blockedPoints.isEmpty()) {
            blockedPoints.forEach(graphPoints::remove);
        }

        graphPoints.forEach(vertex -> {
            List<WeightedEdge> adjacentEdges = originalGraph.getEdgesOfVertex(vertex);
            if (electrified) {
                adjacentEdges = adjacentEdges.stream().filter(WeightedEdge::electrified).toList();
            }
            if (!blockedPoints.isEmpty()) {
                adjacentEdges = adjacentEdges.stream().filter(edge -> !blockedPoints.contains(edge.destination())).toList();
            }
            createVertices(newGraph, vertex, adjacentEdges);
        });

        return newGraph;
    }

    private void createVertices(org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> newGraph, GraphPoint point, List<WeightedEdge> adjacentEdges) {
        if (point instanceof Switch) {
            newGraph.addVertex(new DirectedNode(point, PointSide.IN));
            newGraph.addVertex(new DirectedNode(point, PointSide.OUT));
            for (WeightedEdge edge : adjacentEdges) {
                PointSide sourceSide = ((Switch) point).getSwitchSideFromPoint(edge.destination());
                if (point.equals(edge.destination())) {
                    // Special case if a switch is connected to itself
                    DirectedNode source = new DirectedNode(point, sourceSide.getOpposite());
                    DirectedNode destination = new DirectedNode(point, sourceSide);

                    if (!newGraph.containsEdge(source, destination)) {    // Checks if edge already exists to prevent duplicate edges
                        addEdgeToGraph(newGraph, source, destination, edge.distance().value());
                    }
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
                    if (isEdgeAlreadyConnected(newGraph, edge, edges)) {
                        continue;
                    }
                    addEdge(newGraph, point, PointSide.OUT, edge);
                }
            } else {
                newGraph.addVertex(new DirectedNode(point, PointSide.IN));
                newGraph.addVertex(new DirectedNode(point, PointSide.OUT));
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
        DirectedNode connection1source = new DirectedNode(point, sourceSide.getOpposite());
        DirectedNode connection1destination = new DirectedNode(edge.destination(), destinationSide);
        DirectedNode connection2source = new DirectedNode(edge.destination(), destinationSide.getOpposite());
        DirectedNode connection2destination = new DirectedNode(point, sourceSide);

        addEdgeToGraph(newGraph, connection1source, connection1destination, edge.distance().value());
        addEdgeToGraph(newGraph, connection2source, connection2destination, edge.distance().value());
    }

    private boolean outSideAlreadyConnected(org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> newGraph, GraphPoint point) {
        return newGraph.edgesOf(new DirectedNode(point, PointSide.OUT)).size() > 1;
    }

    private boolean containsGraphPoint(org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> newGraph, GraphPoint point) {
        return newGraph.vertexSet().stream().anyMatch(node -> node.getPoint().equals(point));
    }
}
