package de.dhbw.modellbahn.plugin.routing.alg;

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

    public DefaultMonoTrainRouting(Graph<DirectedNode, DefaultWeightedEdge> routingGraph) {
        this.graph = routingGraph;
    }


    public GraphPath<DirectedNode, DefaultWeightedEdge> findShortestPath(final DirectedNode start, final DirectedNode end) throws PathNotPossibleException {
        Objects.requireNonNull(start, "Start edge must not be null");
        Objects.requireNonNull(end, "End edge must not be null");

        if (!graph.containsVertex(start) || !graph.containsVertex(end)) {
            throw new IllegalArgumentException("Start or end node not in graph");
        }

        System.out.println("start routing");

        ShortestPathAlgorithm<DirectedNode, DefaultWeightedEdge> dijkstraAlgorithm = new DijkstraShortestPath<>(graph);
        GraphPath<DirectedNode, DefaultWeightedEdge> path = dijkstraAlgorithm.getPath(start, end);
        System.out.println("stop routing");
        if (path == null) {
            throw new PathNotPossibleException("No path found between " + start.getNodeName() + " and " + end.getNodeName());
        }
        return path;
    }

    public List<DirectedNode> findShortestPath(final GraphPoint currentPosition, final GraphPoint currentFacingDirection, final GraphPoint destination) throws PathNotPossibleException {


        PointSide direction = PointSide.IN; // TODO find out of current facing direction

        DirectedNode start = new DirectedNode(currentPosition, direction);


        PointSide endDirection = PointSide.IN; // TODO find shortest Facing to destination
        DirectedNode end = new DirectedNode(destination, endDirection);

        GraphPath<DirectedNode, DefaultWeightedEdge> path = findShortestPath(start, end);
        List<DirectedNode> pathAlternative1 = path.getVertexList();

//        endDirection = PointSide.OUT; // TODO find shortest Facing to destination
//        DirectedNode end2 = new DirectedNode(destination, endDirection);
//        List<DirectedNode> pathAlternative2 = findShortestPath(start, end2);
        System.out.println("return");
        return pathAlternative1; //.size() < pathAlternative2.size() ? pathAlternative1 : pathAlternative2; // TODO check if this is the correct way to decide
    }


}
