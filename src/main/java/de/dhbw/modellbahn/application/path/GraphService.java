package de.dhbw.modellbahn.application.path;

import de.dhbw.modellbahn.domain.graph2.DirectedNode;
import de.dhbw.modellbahn.domain.graph2.Route;
import de.dhbw.modellbahn.domain.graph2.StartNode;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;
import java.util.Objects;

public class GraphService {
    private final Graph<DirectedNode, DefaultWeightedEdge> graph;
    private boolean startNodeAdded = false;

    public GraphService() {
        this.graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    }

    public void addRoute(Route route) {
        graph.addVertex(route.start());
        graph.addVertex(route.end());
        graph.setEdgeWeight(graph.addEdge(route.start(), route.end()), route.weight());
    }

    public void addStartNode(StartNode startNode, DirectedNode nextNode) {
        Objects.requireNonNull(startNode, "Start node must not be null");
        Objects.requireNonNull(nextNode, "Next node must not be null");

        if (nextNode instanceof StartNode) {
            throw new IllegalArgumentException("Next node must not be a start node");
        }
        if (startNodeAdded) {
            throw new IllegalStateException("Start node already added");
        }
        graph.addVertex(startNode);
        graph.setEdgeWeight(graph.addEdge(startNode, nextNode), 0);
        startNodeAdded = true;
    }

    public List<DirectedNode> findShortestPath(DirectedNode start, DirectedNode end) throws PathNotPossibleException {
        Objects.requireNonNull(start, "Start node must not be null");
        Objects.requireNonNull(end, "End node must not be null");

        DijkstraShortestPath<DirectedNode, DefaultWeightedEdge> dijkstraAlgorithm = new DijkstraShortestPath<>(graph);
        if (dijkstraAlgorithm.getPath(start, end) == null) {
            throw new PathNotPossibleException("No path found between " + start.getNodeName() + " and " + end.getNodeName());
        }
        return dijkstraAlgorithm.getPath(start, end).getVertexList();
    }
}
