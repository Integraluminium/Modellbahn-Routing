package de.dhbw.modellbahn.plugin.routing.jgrapht.mapper;

import de.dhbw.modellbahn.application.routing.directed.graph.DirectedNode;
import de.dhbw.modellbahn.application.routing.directed.graph.WeightedDistanceEdge;
import de.dhbw.modellbahn.domain.graph.nodes.attributes.Distance;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A mapper class that converts a list of directed nodes to a list of weighted distance edges.
 */
public class DirectedNodeToWeightedEdgeMapper {

    /**
     * Converts the GraphPath to a list of WeightedDistanceEdges
     * <p>
     * This method is needed, because the Information stored in the Edge List is protected!
     *
     * @param path the path to convert
     * @return a list of WeightedDistanceEdges
     */
    public List<WeightedDistanceEdge> getWeightedDistanceEdgesList(final Graph<DirectedNode, DefaultWeightedEdge> graph, final List<DirectedNode> path) {

        if (path.size() < 2) return Collections.emptyList();

        List<WeightedDistanceEdge> edgeList = new ArrayList<>();
        Iterator<DirectedNode> vertexIterator = path.iterator();
        DirectedNode currentNode = vertexIterator.next();
        int distance = 0;
        while (vertexIterator.hasNext()) {
            DirectedNode nextNode = vertexIterator.next();

            edgeList.add(new WeightedDistanceEdge(currentNode.getPoint(), new Distance(distance)));
            distance = (int) Math.round(graph.getEdgeWeight(graph.getEdge(currentNode, nextNode)));

            currentNode = nextNode;
        }

        // add final node
        edgeList.add(new WeightedDistanceEdge(currentNode.getPoint(), new Distance(distance)));

        return edgeList;
    }

}
