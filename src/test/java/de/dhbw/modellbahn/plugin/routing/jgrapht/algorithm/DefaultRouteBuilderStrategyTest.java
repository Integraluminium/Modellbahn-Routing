package de.dhbw.modellbahn.plugin.routing.jgrapht.algorithm;

import de.dhbw.modellbahn.application.routing.building.PathNotPossibleException;
import de.dhbw.modellbahn.application.routing.building.RoutingAlgorithm;
import de.dhbw.modellbahn.application.routing.directed.graph.DirectedNode;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.nodes.attributes.PointSide;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.plugin.routing.jgrapht.mapper.GraphToRoutingGraphMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static de.dhbw.modellbahn.plugin.DomainGraphFactory.createSmallTestGraph;
import static de.dhbw.modellbahn.plugin.DomainGraphFactory.createTestGraph;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DefaultRouteBuilderStrategyTest {

    @Test
    void findShortestPath() throws PathNotPossibleException {
        // Arrange
        Graph graph = createTestGraph();

        // Action
        GraphPoint start = GraphPoint.of("A");
        GraphPoint direction = GraphPoint.of("D");
        GraphPoint end = GraphPoint.of("G");

        GraphToRoutingGraphMapper mapper = new GraphToRoutingGraphMapper(); // TODO toConsider height and electrification
        var routingGraph = mapper.mapGraphToJGraphT(graph);

        ShortestPathFinder shortestPathFinder = new ShortestPathFinder(routingGraph, RoutingAlgorithm.DIJKSTRA);
        List<DirectedNode> shortestPath = shortestPathFinder.findShortestPath(new DirectedNode(start, PointSide.OUT), direction);

        // Assert
        for (DirectedNode directedNode : shortestPath) {
            System.out.println(directedNode.getNodeName());
        }
        assertThat(shortestPath).isNotNull();
        // TODO implement more asserts
    }

    @Test
    @Disabled
    void testIsRoutingWorkingAtAll() throws PathNotPossibleException {
        Graph graph = createSmallTestGraph();

        GraphToRoutingGraphMapper mapper = new GraphToRoutingGraphMapper(); // TODO toConsider height and electrification
        var routingGraph = mapper.mapGraphToJGraphT(graph);

        DirectedNode pointAIn = new DirectedNode(GraphPoint.of("A"), PointSide.IN);
        DirectedNode pointAOut = new DirectedNode(GraphPoint.of("A"), PointSide.OUT);
        DirectedNode pointBIn = new DirectedNode(GraphPoint.of("B"), PointSide.IN);
        DirectedNode pointBOut = new DirectedNode(GraphPoint.of("B"), PointSide.OUT);
        DirectedNode pointCIn = new DirectedNode(GraphPoint.of("C"), PointSide.IN);
        DirectedNode pointZIn = new DirectedNode(GraphPoint.of("Z"), PointSide.IN);

        ShortestPathFinder shortestPathFinder = new ShortestPathFinder(routingGraph, RoutingAlgorithm.DIJKSTRA);
        List<DirectedNode> shortestPath = shortestPathFinder.findShortestPath(pointAIn, pointZIn);

        assertThat(shortestPath).isNotNull();
        assertThat(shortestPath).isEqualTo(List.of(pointAIn, pointBOut, pointCIn, pointBIn, pointAOut, pointZIn));
    }
}