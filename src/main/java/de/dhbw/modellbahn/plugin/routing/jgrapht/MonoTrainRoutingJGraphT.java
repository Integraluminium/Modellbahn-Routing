package de.dhbw.modellbahn.plugin.routing.jgrapht;

import de.dhbw.modellbahn.application.RoutingAlgorithm;
import de.dhbw.modellbahn.application.RoutingOptimization;
import de.dhbw.modellbahn.application.routing.*;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.graph.PointSide;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.plugin.routing.jgrapht.alg.DefaultMonoTrainRoutingStrategy;
import de.dhbw.modellbahn.plugin.routing.jgrapht.mapper.DirectedNodeToWeightedEdgeMapper;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class MonoTrainRoutingJGraphT {
    private static final Logger logger = Logger.getLogger(MonoTrainRoutingJGraphT.class.getSimpleName());
    private final DirectedNodeToWeightedEdgeMapper directedNodeToWeightedEdgeMapper;
    private final org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> routingGraph;
    private final MonoTrainRoutingStrategy strategy;

    public MonoTrainRoutingJGraphT(RoutingAlgorithm algorithm, org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> routingGraph) {
        this.directedNodeToWeightedEdgeMapper = new DirectedNodeToWeightedEdgeMapper();
        this.routingGraph = routingGraph;
        this.strategy = new DefaultMonoTrainRoutingStrategy(routingGraph, algorithm);
    }

    private Route finalizeRouting(final Locomotive locomotive, final List<DirectedNode> path, final GraphPoint newFacingDirection) throws PathNotPossibleException {
        List<WeightedDistanceEdge> weightedDistanceEdges = mapPathToWeightedEdges(path);
        logger.info("Calculated Route: " + weightedDistanceEdges.stream().map(e -> "(" + e.point().getName().name() + " d=" + e.distance().value() + ")").toList());
        RouteGenerator generator = new RouteGenerator(locomotive, weightedDistanceEdges, newFacingDirection);
        return generator.generateRoute();
    }

    public Route generateRoute(Locomotive locomotive, GraphPoint destination, RoutingOptimization optimisations) throws PathNotPossibleException {
        DirectedNode startNode = beforeRouting(locomotive, optimisations);
        List<DirectedNode> path = strategy.findShortestPath(startNode, destination);
        GraphPoint newFacingDirection = determineNewFacingDirection(path);

        return finalizeRouting(locomotive, path, newFacingDirection);
    }

    public Route generateRoute(Locomotive locomotive, GraphPoint destination, GraphPoint destinationFacing, RoutingOptimization optimisations) throws PathNotPossibleException {
        DirectedNode startNode = beforeRouting(locomotive, optimisations);
        DirectedNode endNode = getDirectedNode(destination, destinationFacing);

        List<DirectedNode> path = strategy.findShortestPath(startNode, endNode);

        return finalizeRouting(locomotive, path, destinationFacing);
    }

    private DirectedNode beforeRouting(final Locomotive locomotive, RoutingOptimization optimization) {
        // TODO optimization are not considered
        GraphPoint start = locomotive.getCurrentPosition();
        GraphPoint facingDirection = locomotive.getCurrentFacingDirection();

        return getDirectedNode(start, facingDirection);
    }

    private DirectedNode getDirectedNode(GraphPoint point, GraphPoint facingDirection) {
        PointSide side = getSideFromPoint(point, facingDirection);
        return new DirectedNode(point, side);
    }

    /**
     * Get the side of a point from a given point and facing direction.
     * <toModifySystem>assert that facing Direction is neighbor of point
     */
    private PointSide getSideFromPoint(GraphPoint point, GraphPoint facingDirection) {

        if (isFacingDirectionConnectedToPointAtPointSide(point, facingDirection, PointSide.IN)) {
            return PointSide.IN;
        }

        if (isFacingDirectionConnectedToPointAtPointSide(point, facingDirection, PointSide.OUT)) {
            return PointSide.OUT;
        }
        throw new IllegalArgumentException("Point " + facingDirection + " is not neighbour point of " + point);
    }

    private boolean isFacingDirectionConnectedToPointAtPointSide(final GraphPoint point, final GraphPoint facingDirection, final PointSide pointSide) {
        Set<DefaultWeightedEdge> outgoingEdges = this.routingGraph.edgesOf(new DirectedNode(point, pointSide));
        return outgoingEdges.stream().anyMatch(edge -> this.routingGraph.getEdgeTarget(edge).getPoint().equals(facingDirection));
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
