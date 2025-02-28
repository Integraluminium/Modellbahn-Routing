package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.adapter.routing.DirectedNodeToWeightedEdgeMapper;
import de.dhbw.modellbahn.adapter.routing.GraphMapper;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;
import java.util.Set;

// TODO This class needs tests
public class RouteBuilder {
    private final MonoTrainRouting monoTrainRouting;
    private final DirectedNodeToWeightedEdgeMapper directedNodeToWeightedEdgeMapper;
    private final org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> routingGraph;

    public RouteBuilder(final Graph graph, final MonoTrainRouting monoTrainRouting, final GraphMapper graphMapper, final DirectedNodeToWeightedEdgeMapper directedNodeToWeightedEdgeMapper) {
        this.monoTrainRouting = monoTrainRouting;
        this.directedNodeToWeightedEdgeMapper = directedNodeToWeightedEdgeMapper;

        this.routingGraph = graphMapper.mapGraph(graph);
    }

    public Route calculateRoute(Locomotive locomotive, GraphPoint start, GraphPoint facingDirection, GraphPoint end) throws PathNotPossibleException {
        List<DirectedNode> path = findShortestPath(start, facingDirection, end);
        List<WeightedDistanceEdge> weightedDistanceEdges = mapPathToWeightedEdges(path);
        GraphPoint newFacingDirection = determineNewFacingDirection(path);
        return generateRoute(locomotive, weightedDistanceEdges, newFacingDirection);
    }

    private List<DirectedNode> findShortestPath(final GraphPoint start, final GraphPoint facingDirection, final GraphPoint end) throws PathNotPossibleException {
        return monoTrainRouting.findShortestPath(start, facingDirection, end);
    }

    private List<WeightedDistanceEdge> mapPathToWeightedEdges(final List<DirectedNode> path) {
        return directedNodeToWeightedEdgeMapper.getWeightedDistanceEdgesList(routingGraph, path);
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

    private Route generateRoute(final Locomotive locomotive, final List<WeightedDistanceEdge> weightedDistanceEdges, final GraphPoint newFacingDirection) throws PathNotPossibleException {
        RouteGenerator generator = new RouteGenerator(locomotive, weightedDistanceEdges, newFacingDirection);
        return generator.generateRoute();
    }
}
