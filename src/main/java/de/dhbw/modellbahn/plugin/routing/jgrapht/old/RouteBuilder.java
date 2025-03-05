//package de.dhbw.modellbahn.plugin.routing.jgrapht.old;
//
//import de.dhbw.modellbahn.application.RoutingAlgorithm;
//import de.dhbw.modellbahn.application.routing.DirectedNode;
//import de.dhbw.modellbahn.application.routing.PathNotPossibleException;
//import de.dhbw.modellbahn.application.routing.Route;
//import de.dhbw.modellbahn.application.routing.WeightedDistanceEdge;
//import de.dhbw.modellbahn.domain.graph.GraphPoint;
//import de.dhbw.modellbahn.domain.locomotive.Locomotive;
//import de.dhbw.modellbahn.plugin.routing.jgrapht.MonoTrainRoutingStrategy;
//import de.dhbw.modellbahn.plugin.routing.jgrapht.RouteGeneratorForJGraphT;
//import de.dhbw.modellbahn.plugin.routing.jgrapht.alg.DefaultMonoTrainRoutingStrategy;
//import org.jgrapht.graph.DefaultWeightedEdge;
//
//import java.util.List;
//import java.util.Set;
//
/// / TODO This class needs tests
//public class RouteBuilder {
//    private final RoutingAlgorithm algorithm;
//    private final DirectedNodeToWeightedEdgeMapper directedNodeToWeightedEdgeMapper;
//    private final org.jgrapht.Graph routingGraph;
//
//    public RouteBuilder(RoutingAlgorithm algorithm) {
//        this.algorithm = algorithm;
//        this.directedNodeToWeightedEdgeMapper = new DirectedNodeToWeightedEdgeMapper();
//        this.routingGraph = monoTrainRoutingStrategy.getGraph();
//    }
//
//    private MonoTrainRoutingStrategy getMonoTrainRoutingStrategy(RoutingAlgorithm algorithm) {
//        switch (algorithm) {
//            case DIJKSTRA:
//                return new DefaultMonoTrainRoutingStrategy(routingGraph);
//            default:
//                throw new IllegalArgumentException("The algorithm " + algorithm + " is not supported.");
//        }
//    }
//
/// /    public Route calculateRoute(Locomotive locomotive, GraphPoint start, GraphPoint facingDirection, GraphPoint end) throws PathNotPossibleException {
/// /        MonoTrainRoutingStrategy monoTrainRoutingStrategy = getMonoTrainRoutingStrategy(algorithm);
/// /        List<DirectedNode> path = monoTrainRoutingStrategy.findShortestPath(start, facingDirection, end);
/// /        List<WeightedDistanceEdge> weightedDistanceEdges = mapPathToWeightedEdges(path);
/// /        GraphPoint newFacingDirection = determineNewFacingDirection(path);
/// /        return generateRoute(locomotive, weightedDistanceEdges, newFacingDirection);
/// /    }
//
//    public void calculateRouteForLocomotives() {
//        GraphMapper mapper = new GraphMapper();
//        org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> jGraphTGraph = mapper.mapGraphToJGraphT(graph);
//
//
//        for (RouteGeneratorForJGraphT.LocomotiveInfo locInfo : this.locomotivesToConsiderInRouting.values()) {
//            if (locInfo.getDestination() == null) {
//                continue;
//            }
//            Locomotive loc = locInfo.getLoc();
//            try {
//                List<DirectedNode> x = monoTrainRoutingStrategy.findShortestPath(loc.getCurrentPosition(), loc.getCurrentFacingDirection(), locInfo.getDestination());
//
//            } catch (PathNotPossibleException e) {
//                e.printStackTrace(); // TODO TOFU
//            }
//        }
//    }
//
//    private List<WeightedDistanceEdge> mapPathToWeightedEdges(final List<DirectedNode> path) {
//        return directedNodeToWeightedEdgeMapper.getWeightedDistanceEdgesList(routingGraph, path);
//    }
//
//    private GraphPoint determineNewFacingDirection(final List<DirectedNode> path) {
//        if (routingGraph.outDegreeOf(path.getLast()) == 0) {
//            throw new IllegalArgumentException("The last node of the path has no outgoing edges.");
//        }
//        Set<DefaultWeightedEdge> edges = routingGraph.outgoingEdgesOf(path.getLast());
//        DefaultWeightedEdge weightedEdge = edges.stream().toList().get(0);
//        DirectedNode randomNextNodeConnectedToLastNodeUsedToObtainDirection = routingGraph.getEdgeTarget(weightedEdge);
//        return randomNextNodeConnectedToLastNodeUsedToObtainDirection.getPoint();
//    }
//
//    private Route generateRoute(final Locomotive locomotive, final List<WeightedDistanceEdge> weightedDistanceEdges, final GraphPoint newFacingDirection) throws PathNotPossibleException {
//        RouteCreatorOld generator = new RouteCreatorOld(locomotive, weightedDistanceEdges, newFacingDirection);
//        return generator.generateRoute();
//    }
//}
