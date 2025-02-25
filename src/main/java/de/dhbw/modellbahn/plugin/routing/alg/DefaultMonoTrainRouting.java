package de.dhbw.modellbahn.plugin.routing.alg;

import de.dhbw.modellbahn.application.routing.DirectedNode;
import de.dhbw.modellbahn.application.routing.MonoTrainRouting;
import de.dhbw.modellbahn.application.routing.PathNotPossibleException;
import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;
import java.util.Objects;

public class DefaultMonoTrainRouting implements MonoTrainRouting {
    private final Graph<DirectedNode, DefaultWeightedEdge> graph;

    public DefaultMonoTrainRouting() {
        this.graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    }

    // TODO needs extendable weight parameter and a function to calculate the weight of an edge
    void addEdge(DirectedNode start, DirectedNode end, double weight) {
        Objects.requireNonNull(start, "Start point must not be null");
        Objects.requireNonNull(end, "End point must not be null");

        graph.addVertex(start);
        graph.addVertex(end);

        DefaultWeightedEdge edge = graph.addEdge(start, end);
        graph.setEdgeWeight(edge, weight);
    }


    @Override
    public List<DirectedNode> findShortestPath(final DirectedNode start, final DirectedNode end) throws PathNotPossibleException {
        Objects.requireNonNull(start, "Start edge must not be null");
        Objects.requireNonNull(end, "End edge must not be null");

        ShortestPathAlgorithm<DirectedNode, DefaultWeightedEdge> dijkstraAlgorithm = new DijkstraShortestPath<>(graph);
        if (dijkstraAlgorithm.getPath(start, end) == null) {
            throw new PathNotPossibleException("No path found between " + start.getNodeName() + " and " + end.getNodeName());
        }
        return dijkstraAlgorithm.getPath(start, end).getVertexList();
    }


}
