package de.dhbw.modellbahn.plugin.routing.alg;

import de.dhbw.modellbahn.application.routing.DirectedNode;
import de.dhbw.modellbahn.application.routing.MonoTrainRouting;
import de.dhbw.modellbahn.application.routing.PathNotPossibleException;
import de.dhbw.modellbahn.application.routing.WeightedDistanceEdge;
import de.dhbw.modellbahn.domain.graph.Distance;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.graph.PointSide;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;

public class DefaultMonoTrainRouting implements MonoTrainRouting {
    private final Graph<DirectedNode, DefaultWeightedEdge> graph;

    public DefaultMonoTrainRouting(Graph<DirectedNode, DefaultWeightedEdge> routingGraph) {
        this.graph = routingGraph;
    }

    /**
     * Converts the GraphPath to a list of WeightedDistanceEdges
     * <p>
     * This method is needed, because the Information stored in the Edge List is protected!
     *
     * @param path the path to convert
     * @return a list of WeightedDistanceEdges
     */
    protected static List<WeightedDistanceEdge> getWeightedDistanceEdgesList(final GraphPath<DirectedNode, DefaultWeightedEdge> path) {
        List<DirectedNode> vertexList = path.getVertexList();
        if (vertexList.size() < 2) return Collections.emptyList();

        Graph<DirectedNode, DefaultWeightedEdge> graph1 = path.getGraph();
        List<WeightedDistanceEdge> edgeList = new ArrayList<>();
        Iterator<DirectedNode> vertexIterator = vertexList.iterator();
        DirectedNode currentNode = vertexIterator.next();
        while (vertexIterator.hasNext()) {
            DirectedNode nextNode = vertexIterator.next();

            int distance = (int) Math.round(graph1.getEdgeWeight(graph1.getEdge(currentNode, nextNode)));
            edgeList.add(new WeightedDistanceEdge(currentNode.getPoint(), new Distance(distance)));

            currentNode = nextNode;
        }
        return edgeList;
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
    public List<WeightedDistanceEdge> findShortestPath(final DirectedNode start, final DirectedNode end) throws PathNotPossibleException {
        Objects.requireNonNull(start, "Start edge must not be null");
        Objects.requireNonNull(end, "End edge must not be null");

        if (!graph.containsVertex(start) || !graph.containsVertex(end)) {
            throw new IllegalArgumentException("Start or end node not in graph");
        }

        DijkstraShortestPath<DirectedNode, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        GraphPath<DirectedNode, DefaultWeightedEdge> path = runShortestPathForExactDirection(dijkstraShortestPath, start, end);
        return getWeightedDistanceEdgesList(path);
    }

    @Override
    public List<WeightedDistanceEdge> findShortestPath(final DirectedNode start, final GraphPoint destination) throws PathNotPossibleException {
        Objects.requireNonNull(start, "Start edge must not be null");
        Objects.requireNonNull(destination, "End edge must not be null");

        DirectedNode preferredEnd = new DirectedNode(destination, PointSide.IN);
        DirectedNode alternativeEnd = new DirectedNode(destination, PointSide.OUT);


        DijkstraShortestPath<DirectedNode, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<DirectedNode, DefaultWeightedEdge> path = runShortestPathWithAlternative(dijkstraShortestPath, start, preferredEnd, alternativeEnd);

        return getWeightedDistanceEdgesList(path);
    }


    public List<WeightedDistanceEdge> findShortestPath(final GraphPoint start, final GraphPoint facingDirection, final GraphPoint end) throws PathNotPossibleException {
        DirectedNode startNode = new DirectedNode(start, PointSide.OUT); // TODO Determine the correct direction with facingDirection
        return findShortestPath(startNode, end);
    }
}
