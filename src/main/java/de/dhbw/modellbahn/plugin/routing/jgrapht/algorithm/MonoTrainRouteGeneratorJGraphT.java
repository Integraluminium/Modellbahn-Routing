package de.dhbw.modellbahn.plugin.routing.jgrapht.algorithm;

import de.dhbw.modellbahn.application.routing.Route;
import de.dhbw.modellbahn.application.routing.RouteGenerator;
import de.dhbw.modellbahn.application.routing.actions.LocToggleAction;
import de.dhbw.modellbahn.application.routing.actions.RoutingAction;
import de.dhbw.modellbahn.application.routing.building.PathNotPossibleException;
import de.dhbw.modellbahn.application.routing.building.RoutingAlgorithm;
import de.dhbw.modellbahn.application.routing.building.RoutingOptimization;
import de.dhbw.modellbahn.application.routing.directed.graph.DirectedNode;
import de.dhbw.modellbahn.application.routing.directed.graph.WeightedDistanceEdge;
import de.dhbw.modellbahn.domain.graph.nodes.attributes.PointSide;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.BufferStop;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.plugin.routing.jgrapht.mapper.DirectedNodeToWeightedEdgeMapper;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class MonoTrainRouteGeneratorJGraphT {
    private static final Logger logger = Logger.getLogger(MonoTrainRouteGeneratorJGraphT.class.getSimpleName());
    private final DirectedNodeToWeightedEdgeMapper directedNodeToWeightedEdgeMapper;
    private final org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> routingGraph;
    private final ShortestPathFinder shortestPathFinder;

    public MonoTrainRouteGeneratorJGraphT(RoutingAlgorithm algorithm, org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> routingGraph) {
        this.directedNodeToWeightedEdgeMapper = new DirectedNodeToWeightedEdgeMapper();
        this.routingGraph = routingGraph;
        this.shortestPathFinder = new ShortestPathFinder(routingGraph, algorithm);
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
        if (destination.equals(locomotive.getCurrentFacingDirection())){
            return calculateRouteToFacingDirection(locomotive, destination, optimisations);
        }
        DirectedNode startNode = beforeRouting(locomotive, optimisations);
        try {
            List<DirectedNode> path = shortestPathFinder.findShortestPath(startNode, destination);
            try {
                GraphPoint newFacingDirection = determineNewFacingDirection(path);
                return finalizeRouting(locomotive, path, newFacingDirection);

            } catch (PathNotPossibleException e) {
                // Last node is a buffer stop, toggle facing direction
                if (!(path.getLast().getPoint() instanceof BufferStop)) {
                    throw new PathNotPossibleException("Last node is not a buffer stop and no facing direction could be determined.");
                }

                DirectedNode secondLastNode = path.get(path.size() - 2);
                RoutingAction toggleAction = new LocToggleAction(locomotive, secondLastNode.getPoint());
                Route route = finalizeRouting(locomotive, path, secondLastNode.getPoint());

                return route.addAction(toggleAction);
            }
        } catch (PathNotPossibleException e) {
            // check if destination could be reached by toggling the facing direction
            DirectedNode tempNode = new DirectedNode(startNode.getPoint(), startNode.getSide().getOpposite());
            List<DirectedNode> path = shortestPathFinder.findShortestPath(tempNode, destination);
            if (path.isEmpty()) {
                throw e; // rethrow original exception
            }

            return calculateRouteUsingBufferStop(locomotive, destination, startNode);
        }
    }

    private Route calculateRouteToFacingDirection(Locomotive locomotive, GraphPoint destination, RoutingOptimization optimisations) throws PathNotPossibleException {
        List<DirectedNode> path = new ArrayList<>();
        DirectedNode startNode = getDirectedNode(locomotive.getCurrentPosition(), destination);
        path.add(startNode);
        DirectedNode endNode = beforeRouting(locomotive, optimisations);
        path.add(endNode);
        return finalizeRouting(locomotive, path, determineNewFacingDirection(path));
    }

    private Route calculateRouteUsingBufferStop(final Locomotive locomotive, final GraphPoint destination, final DirectedNode startNode) throws PathNotPossibleException {
        // find nearest buffer stop

        List<List<DirectedNode>> possiblePaths = new ArrayList<>();
        for (DirectedNode bufferStopNode : routingGraph.vertexSet().stream().filter(node -> node.getPoint() instanceof BufferStop).toList()) {
            try {
                List<DirectedNode> bufferStopPath = shortestPathFinder.findShortestPath(startNode, bufferStopNode);
                if (!bufferStopPath.isEmpty()) {
                    possiblePaths.add(bufferStopPath);
                }
            } catch (PathNotPossibleException e) {
                // ignore, because not all buffer stops must be reachable
            }
        }

        List<DirectedNode> pathToBufferStop = possiblePaths.stream().min((nodeList1, nodeList2) ->
                        Long.compare(sumOfPathDistances(nodeList1), sumOfPathDistances(nodeList2)))
                .orElseThrow(() -> new PathNotPossibleException("No path to buffer stop found"));

        GraphPoint newFacingDirection;
        if (pathToBufferStop.size() < 2) {
            newFacingDirection = locomotive.getCurrentPosition();
        } else {
            newFacingDirection = pathToBufferStop.get(pathToBufferStop.size() - 2).getPoint();
        }

        DirectedNode bufferStopNode = pathToBufferStop.get(pathToBufferStop.size() - 1);

        Route routeToBufferStop = finalizeRouting(locomotive, pathToBufferStop, newFacingDirection);

        // route from buffer stop to destination
        DirectedNode invertedBufferStopNode = new DirectedNode(bufferStopNode.getPoint(), bufferStopNode.getSide().getOpposite());
        List<DirectedNode> pathToDestination = shortestPathFinder.findShortestPath(invertedBufferStopNode, destination);
        GraphPoint endFacingDirection = determineNewFacingDirection(pathToDestination);

        Route routeToDestination = finalizeRouting(locomotive, pathToDestination, endFacingDirection);

        return routeToBufferStop.addAction(new LocToggleAction(locomotive, newFacingDirection)).addRoute(routeToDestination);
    }

    private long sumOfPathDistances(final List<DirectedNode> nodeList) {
        return mapPathToWeightedEdges(nodeList).stream().mapToLong(edge -> ((long) edge.distance().value())).sum();
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
        if (destination.equals(locomotive.getCurrentFacingDirection())){
            return calculateRouteToFacingDirection(locomotive, destination, optimisations);
        }
        DirectedNode startNode = beforeRouting(locomotive, optimisations);
        DirectedNode endNode = getDirectedNode(destination, destinationFacing);

        List<DirectedNode> path = shortestPathFinder.findShortestPath(startNode, endNode);

        return finalizeRouting(locomotive, path, destinationFacing);
    }

    private DirectedNode beforeRouting(final Locomotive locomotive, RoutingOptimization optimization) {
        // TODO optimization are not considered
        GraphPoint start = locomotive.getCurrentPosition();
        GraphPoint facingDirection = locomotive.getCurrentFacingDirection();


        DirectedNode currentPosition = getDirectedNode(start, facingDirection);
        return this.routingGraph.outgoingEdgesOf(currentPosition).stream()
                .filter(edge -> this.routingGraph.getEdgeTarget(edge).getPoint().equals(facingDirection))
                .map(this.routingGraph::getEdgeTarget)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No edge found for start node " + start + " facing " + facingDirection));
    }


    private Route finalizeRouting(final Locomotive locomotive, final List<DirectedNode> path, final GraphPoint newFacingDirection) throws PathNotPossibleException {
        GraphPoint start = locomotive.getCurrentPosition();
        GraphPoint facingDirection = locomotive.getCurrentFacingDirection();
        DirectedNode currentStartPosition = getDirectedNode(start, facingDirection);

        if (path.get(0).getPoint().equals(facingDirection)) {
            path.add(0, currentStartPosition);
        }

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
