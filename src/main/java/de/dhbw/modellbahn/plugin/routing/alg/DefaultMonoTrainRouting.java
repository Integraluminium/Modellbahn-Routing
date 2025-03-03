package de.dhbw.modellbahn.plugin.routing.alg;

import de.dhbw.modellbahn.adapter.routing.GraphMapper;
import de.dhbw.modellbahn.application.routing.DirectedNode;
import de.dhbw.modellbahn.application.routing.MonoTrainRouting;
import de.dhbw.modellbahn.application.routing.PathNotPossibleException;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.graph.PointSide;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;
import java.util.Objects;

public class DefaultMonoTrainRouting implements MonoTrainRouting {
    private final Graph<DirectedNode, DefaultWeightedEdge> graph;

    public DefaultMonoTrainRouting(de.dhbw.modellbahn.domain.graph.Graph graph) {
        GraphMapper graphMapper = new GraphMapper();
        this.graph = graphMapper.mapGraphToJGraphT(graph);
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
    protected static GraphPath<DirectedNode, DefaultWeightedEdge> runShortestPathForExactDirection(final ShortestPathAlgorithm<DirectedNode, DefaultWeightedEdge> shortestPathAlgorithm, final DirectedNode start, final DirectedNode end) throws PathNotPossibleException {
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
    protected static GraphPath<DirectedNode, DefaultWeightedEdge> runShortestPathWithAlternative(final ShortestPathAlgorithm<DirectedNode, DefaultWeightedEdge> shortestPathAlgorithm, final DirectedNode start, final DirectedNode preferredEnd, final DirectedNode alternativeEnd) throws PathNotPossibleException {
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

    @Override
    public List<DirectedNode> findShortestPath(final DirectedNode start, final DirectedNode end) throws PathNotPossibleException {
        Objects.requireNonNull(start, "Start edge must not be null");
        Objects.requireNonNull(end, "End edge must not be null");

        if (!graph.containsVertex(start) || !graph.containsVertex(end)) {
            throw new IllegalArgumentException("Start or end node not in graph");
        }

        DijkstraShortestPath<DirectedNode, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        GraphPath<DirectedNode, DefaultWeightedEdge> path = runShortestPathForExactDirection(dijkstraShortestPath, start, end);
        return path.getVertexList();
    }

    @Override
    public List<DirectedNode> findShortestPath(final DirectedNode start, final GraphPoint destination) throws PathNotPossibleException {
        Objects.requireNonNull(start, "Start edge must not be null");
        Objects.requireNonNull(destination, "End edge must not be null");

        DirectedNode preferredEnd = new DirectedNode(destination, PointSide.IN);
        DirectedNode alternativeEnd = new DirectedNode(destination, PointSide.OUT);


        DijkstraShortestPath<DirectedNode, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<DirectedNode, DefaultWeightedEdge> path = runShortestPathWithAlternative(dijkstraShortestPath, start, preferredEnd, alternativeEnd);

        return path.getVertexList();
    }

    @Override
    public List<DirectedNode> findShortestPath(final GraphPoint start, final GraphPoint facingDirection, final GraphPoint end) throws PathNotPossibleException {
        DirectedNode startNode = new DirectedNode(start, PointSide.OUT); // TODO Determine the correct direction with facingDirection
        return findShortestPath(startNode, end);
    }
}
