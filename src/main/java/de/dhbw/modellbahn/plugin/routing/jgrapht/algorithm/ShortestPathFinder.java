package de.dhbw.modellbahn.plugin.routing.jgrapht.algorithm;

import de.dhbw.modellbahn.application.routing.building.PathNotPossibleException;
import de.dhbw.modellbahn.application.routing.building.RoutingAlgorithm;
import de.dhbw.modellbahn.application.routing.directed.graph.DirectedNode;
import de.dhbw.modellbahn.domain.graph.nodes.attributes.PointSide;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.BellmanFordShortestPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;
import java.util.Objects;

public class ShortestPathFinder {
    private final Graph<DirectedNode, DefaultWeightedEdge> graph;
    private final ShortestPathAlgorithm<DirectedNode, DefaultWeightedEdge> shortestPathAlgorithm;

    public ShortestPathFinder(final Graph<DirectedNode, DefaultWeightedEdge> graph, final RoutingAlgorithm algorithm) {
        this.graph = graph;
        this.shortestPathAlgorithm = getMonoTrainRoutingStrategy(algorithm);
    }

    /**
     * Runs the shortest path algorithm for the exact direction
     *
     * @param shortestPathAlgorithm the algorithm containing the graph
     * @param start                 the start node (with direction)
     * @param end                   the end node (with direction)
     * @return the path in graph_t format
     * @throws PathNotPossibleException if no path is found
     */
    protected static GraphPath<DirectedNode, DefaultWeightedEdge> runShortestPathForExactDirection(
            final ShortestPathAlgorithm<DirectedNode, DefaultWeightedEdge> shortestPathAlgorithm, final DirectedNode start,
            final DirectedNode end) throws PathNotPossibleException {
        GraphPath<DirectedNode, DefaultWeightedEdge> path = shortestPathAlgorithm.getPath(start, end);
        if (path == null) {
            throw new PathNotPossibleException("No path found between " + start.getNodeName() + " and " + end.getNodeName());
        }
        return path;
    }

    /**
     * Runs the shortest path algorithm for the preferred direction and if not possible for the alternative direction
     *
     * @param shortestPathAlgorithm the algorithm containing the graph
     * @param start                 the start node (with direction)
     * @param preferredEnd          the preferred end node (with direction)
     * @param alternativeEnd        the alternative end node (with direction)
     * @return the path in graph_t format
     * @throws PathNotPossibleException if no path is found
     */
    protected static GraphPath<DirectedNode, DefaultWeightedEdge> runShortestPathWithAlternative(
            final ShortestPathAlgorithm<DirectedNode, DefaultWeightedEdge> shortestPathAlgorithm, final DirectedNode start,
            final DirectedNode preferredEnd, final DirectedNode alternativeEnd) throws PathNotPossibleException {
        // Different implementation as runShortestPathForExactDirection, therefore no call to that method
        ShortestPathAlgorithm.SingleSourcePaths<DirectedNode, DefaultWeightedEdge> singleSourcePaths = shortestPathAlgorithm.getPaths(start);

        GraphPath<DirectedNode, DefaultWeightedEdge> preferredPath = singleSourcePaths.getPath(preferredEnd);
        GraphPath<DirectedNode, DefaultWeightedEdge> alternativePath = singleSourcePaths.getPath(alternativeEnd);

        if (preferredPath == null && alternativePath == null) {
            throw new PathNotPossibleException("No path found between " + start.getNodeName() + " and " + preferredEnd.getNodeName() + " or " + alternativeEnd.getNodeName());
        } else if (preferredPath == null) {
            return alternativePath;
        } else if (alternativePath == null) {
            return preferredPath;
        } else {
            return preferredPath.getWeight() < alternativePath.getWeight() ? preferredPath : alternativePath;
        }
    }

    private ShortestPathAlgorithm<DirectedNode, DefaultWeightedEdge> getMonoTrainRoutingStrategy(RoutingAlgorithm algorithm) {
        return switch (algorithm) {
            case DIJKSTRA -> new DijkstraShortestPath<>(graph);
            case BELLMAN_FORD -> new BellmanFordShortestPath<>(graph);
        };
    }

    public List<DirectedNode> findShortestPath(final DirectedNode start, final DirectedNode end) throws
            PathNotPossibleException {
        Objects.requireNonNull(start, "Start edge must not be null");
        Objects.requireNonNull(end, "End edge must not be null");

        if (!graph.containsVertex(start) || !graph.containsVertex(end)) {
            throw new IllegalArgumentException("Start or end node not in graph");
        }
        GraphPath<DirectedNode, DefaultWeightedEdge> path = runShortestPathForExactDirection(shortestPathAlgorithm, start, end);
        return path.getVertexList();
    }

    public List<DirectedNode> findShortestPath(final DirectedNode start, final GraphPoint destination) throws
            PathNotPossibleException {
        Objects.requireNonNull(start, "Start edge must not be null");
        Objects.requireNonNull(destination, "End edge must not be null");

        DirectedNode preferredEnd = new DirectedNode(destination, PointSide.IN);
        DirectedNode alternativeEnd = new DirectedNode(destination, PointSide.OUT);

        GraphPath<DirectedNode, DefaultWeightedEdge> path = runShortestPathWithAlternative(shortestPathAlgorithm, start, preferredEnd, alternativeEnd);
        return path.getVertexList();
    }
}
