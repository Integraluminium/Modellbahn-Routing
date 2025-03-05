package de.dhbw.modellbahn.plugin.routing.jgrapht;

import de.dhbw.modellbahn.application.RoutingAlgorithm;
import de.dhbw.modellbahn.application.routing.DirectedNode;
import de.dhbw.modellbahn.application.routing.PathNotPossibleException;
import de.dhbw.modellbahn.application.routing.Route;
import de.dhbw.modellbahn.application.routing.WeightedDistanceEdge;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.plugin.routing.jgrapht.alg.DefaultMonoTrainRoutingStrategy;
import de.dhbw.modellbahn.plugin.routing.jgrapht.old.DirectedNodeToWeightedEdgeMapper;
import de.dhbw.modellbahn.plugin.routing.jgrapht.old.RouteCreator;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;
import java.util.Set;

public class MonoTrainRoutingWrapper {
    private final DirectedNodeToWeightedEdgeMapper directedNodeToWeightedEdgeMapper;
    private final org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> routingGraph;
    private final MonoTrainRoutingStrategy strategy;

    public MonoTrainRoutingWrapper(RoutingAlgorithm algorithm, org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> routingGraph) {
        this.directedNodeToWeightedEdgeMapper = new DirectedNodeToWeightedEdgeMapper();
        this.routingGraph = routingGraph;
        this.strategy = new DefaultMonoTrainRoutingStrategy(routingGraph, algorithm);
    }

    public Route generateRoute(Locomotive locomotive, GraphPoint end) throws PathNotPossibleException {
        GraphPoint start = locomotive.getCurrentPosition();
        GraphPoint facingDirection = locomotive.getCurrentFacingDirection();
        List<DirectedNode> path = strategy.findShortestPath(start, facingDirection, end);
        List<WeightedDistanceEdge> weightedDistanceEdges = mapPathToWeightedEdges(path);
        GraphPoint newFacingDirection = determineNewFacingDirection(path);

        RouteCreator generator = new RouteCreator(locomotive, weightedDistanceEdges, newFacingDirection);
        return generator.generateRoute();
    }


    private List<WeightedDistanceEdge> mapPathToWeightedEdges(final List<DirectedNode> path) {
        return this.directedNodeToWeightedEdgeMapper.getWeightedDistanceEdgesList(routingGraph, path);
    }

    private GraphPoint determineNewFacingDirection(final List<DirectedNode> path) {
        if (routingGraph.outDegreeOf(path.getLast()) == 0) {
            throw new IllegalArgumentException("The last node of the path has no outgoing edges.");
        }
        Set<DefaultWeightedEdge> edges = routingGraph.outgoingEdgesOf(path.getLast());
        DefaultWeightedEdge weightedEdge = edges.stream().toList().get(0);
        DirectedNode randomNextNodeConnectedToLastNodeUsedToObtainDirection = routingGraph.getEdgeTarget(weightedEdge);
        return randomNextNodeConnectedToLastNodeUsedToObtainDirection.getPoint();
    }
}
