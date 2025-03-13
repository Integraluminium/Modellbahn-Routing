package de.dhbw.modellbahn.plugin.routing.jgrapht;

import de.dhbw.modellbahn.application.RoutingAlgorithm;
import de.dhbw.modellbahn.application.RoutingOptimization;
import de.dhbw.modellbahn.application.routing.*;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.plugin.routing.jgrapht.alg.DefaultMonoTrainRoutingStrategy;
import de.dhbw.modellbahn.plugin.routing.jgrapht.mapper.DirectedNodeToWeightedEdgeMapper;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;
import java.util.Set;

public class MonoTrainRoutingJGraphT {
    private final DirectedNodeToWeightedEdgeMapper directedNodeToWeightedEdgeMapper;
    private final org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> routingGraph;
    private final MonoTrainRoutingStrategy strategy;

    public MonoTrainRoutingJGraphT(RoutingAlgorithm algorithm, org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> routingGraph) {
        this.directedNodeToWeightedEdgeMapper = new DirectedNodeToWeightedEdgeMapper();
        this.routingGraph = routingGraph;
        this.strategy = new DefaultMonoTrainRoutingStrategy(routingGraph, algorithm);
    }

    public Route generateRoute(Locomotive locomotive, GraphPoint end, RoutingOptimization optimisations) throws PathNotPossibleException {
        // TODO optimisations are not considered
        GraphPoint start = locomotive.getCurrentPosition();
        GraphPoint facingDirection = locomotive.getCurrentFacingDirection();
        List<DirectedNode> path = strategy.findShortestPath(start, facingDirection, end);
        List<WeightedDistanceEdge> weightedDistanceEdges = mapPathToWeightedEdges(path);
        GraphPoint newFacingDirection = determineNewFacingDirection(path);

        System.out.println("Calculated Route: " + weightedDistanceEdges.stream().map(e -> "(" + e.point().getName().name() + " d=" + e.distance().value() + ")").toList()); // TODO Logging

        RouteGenerator generator = new RouteGenerator(locomotive, weightedDistanceEdges, newFacingDirection);
        return generator.generateRoute();
    }


    private List<WeightedDistanceEdge> mapPathToWeightedEdges(final List<DirectedNode> path) {
        return this.directedNodeToWeightedEdgeMapper.getWeightedDistanceEdgesList(routingGraph, path);
    }

    private GraphPoint determineNewFacingDirection(final List<DirectedNode> path) {
        DirectedNode lastNode = path.getLast();
        if (routingGraph.outDegreeOf(lastNode) == 0) {
            throw new IllegalArgumentException("The last node (" + lastNode.getNodeName() + ") of the path has no outgoing edges.");
        }
        Set<DefaultWeightedEdge> edges = routingGraph.outgoingEdgesOf(lastNode);
        DefaultWeightedEdge weightedEdge = edges.stream().toList().get(0);
        DirectedNode randomNextNodeConnectedToLastNodeUsedToObtainDirection = routingGraph.getEdgeTarget(weightedEdge);
        return randomNextNodeConnectedToLastNodeUsedToObtainDirection.getPoint();
    }
}
