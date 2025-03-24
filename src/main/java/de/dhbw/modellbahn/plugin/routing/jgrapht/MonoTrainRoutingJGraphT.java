package de.dhbw.modellbahn.plugin.routing.jgrapht;

import de.dhbw.modellbahn.application.RoutingAlgorithm;
import de.dhbw.modellbahn.application.RoutingOptimization;
import de.dhbw.modellbahn.application.routing.*;
import de.dhbw.modellbahn.application.routing.action.LocToggleAction;
import de.dhbw.modellbahn.application.routing.action.RoutingAction;
import de.dhbw.modellbahn.domain.graph.BufferStop;
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

    /**
     * Generate a route for a locomotive to a destination <b>without</b> explicit facing direction.
     * Automatic toggle of facing direction at buffer stops.
     *
     * @param locomotive    the locomotive to route
     * @param destination   the destination to route to
     * @param optimisations the optimisations to apply
     * @return the generated route
     * @throws PathNotPossibleException if no path could be found
     */
    public Route generateRoute(Locomotive locomotive, GraphPoint destination, RoutingOptimization optimisations) throws PathNotPossibleException {
        DirectedNode startNode = beforeRouting(locomotive, optimisations);
        List<DirectedNode> path = strategy.findShortestPath(startNode, destination);

        try {
            GraphPoint newFacingDirection = determineNewFacingDirection(path);
            return finalizeRouting(locomotive, path, newFacingDirection);

        } catch (PathNotPossibleException e) {
            if (!(path.getLast().getPoint() instanceof BufferStop)) {
                throw new PathNotPossibleException("Last node is not a buffer stop and no facing direction could be determined.");
            }

            DirectedNode secondLastNode = path.get(path.size() - 2);
            RoutingAction toggleAction = new LocToggleAction(locomotive, secondLastNode.getPoint());
            Route route = finalizeRouting(locomotive, path, secondLastNode.getPoint());

            return route.addAction(toggleAction);
        }
    }

    /**
     * Generate a route for a locomotive to a destination <b>with</b> explicit facing direction.
     * Could not route to a buffer stop, as FacingDirection cannot be reached in buffer stops without toggle.
     *
     * @param locomotive        the locomotive to route
     * @param destination       the destination to route to
     * @param destinationFacing the facing direction at the destination
     * @param optimisations     the optimisations to apply
     * @return the generated route
     * @throws PathNotPossibleException if no path could be found
     */
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


    private Route finalizeRouting(final Locomotive locomotive, final List<DirectedNode> path, final GraphPoint newFacingDirection) throws PathNotPossibleException {
        List<WeightedDistanceEdge> weightedDistanceEdges = mapPathToWeightedEdges(path);
        logger.info("Calculated Route: " + weightedDistanceEdges.stream().map(e -> "(" + e.point().getName().name() + " d=" + e.distance().value() + ")").toList());
        RouteGenerator generator = new RouteGenerator(locomotive, weightedDistanceEdges, newFacingDirection);
        return generator.generateRoute();
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

    private GraphPoint determineNewFacingDirection(final List<DirectedNode> path) throws PathNotPossibleException {
        DirectedNode lastNode = path.getLast();
        if (routingGraph.outDegreeOf(lastNode) == 0) {
            // Buffer stop or blocked rail has no outgoing edges
            throw new PathNotPossibleException("The last node (" + lastNode.getNodeName() + ") of the path has no outgoing edges.");
        }
        Set<DefaultWeightedEdge> edges = routingGraph.outgoingEdgesOf(lastNode);
        DefaultWeightedEdge weightedEdge = edges.stream().toList().get(0);
        DirectedNode randomNextNodeConnectedToLastNodeUsedToObtainDirection = routingGraph.getEdgeTarget(weightedEdge);
        return randomNextNodeConnectedToLastNodeUsedToObtainDirection.getPoint();
    }
}
