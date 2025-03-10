package de.dhbw.modellbahn.plugin.routing.jgrapht.alg;

import de.dhbw.modellbahn.adapter.moba.communication.ApiService;
import de.dhbw.modellbahn.adapter.moba.communication.calls.TrackComponentCallsAdapter;
import de.dhbw.modellbahn.adapter.track_generation.GraphGenerator;
import de.dhbw.modellbahn.application.RoutingAlgorithm;
import de.dhbw.modellbahn.application.port.moba.communication.TrackComponentCalls;
import de.dhbw.modellbahn.application.routing.DirectedNode;
import de.dhbw.modellbahn.application.routing.PathNotPossibleException;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.graph.PointName;
import de.dhbw.modellbahn.domain.graph.PointSide;
import de.dhbw.modellbahn.plugin.MockedConfigReader;
import de.dhbw.modellbahn.plugin.MockedConfigReader_smallGraph;
import de.dhbw.modellbahn.plugin.routing.jgrapht.old.GraphToRoutingGraphMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DefaultRouteBuilderStrategyTest {

    private static Graph createTestGraph() {
        ApiService apiService = new ApiService(0);
        TrackComponentCalls calls = new TrackComponentCallsAdapter(apiService);
        GraphGenerator generator = new GraphGenerator(new MockedConfigReader(), calls);
        return generator.generateGraph();
    }

    private static Graph createSmallTestGraph() {
        ApiService apiService = new ApiService(0);
        TrackComponentCalls calls = new TrackComponentCallsAdapter(apiService);
        GraphGenerator generator = new GraphGenerator(new MockedConfigReader_smallGraph(), calls);
        return generator.generateGraph();
    }

    static GraphPoint createGraphPoint(String name) {
        return new GraphPoint(new PointName(name));
    }

    @Test
    void findShortestPath() throws PathNotPossibleException {
        // Arrange
        Graph graph = createTestGraph();

        // Action
        GraphPoint start = createGraphPoint("A");
        GraphPoint direction = createGraphPoint("D");
        GraphPoint end = createGraphPoint("G");

        GraphToRoutingGraphMapper mapper = new GraphToRoutingGraphMapper(); // TODO consider height and electrification
        var routingGraph = mapper.mapGraphToJGraphT(graph);

        DefaultMonoTrainRoutingStrategy routing = new DefaultMonoTrainRoutingStrategy(routingGraph, RoutingAlgorithm.DIJKSTRA);
        routing.findShortestPath(new DirectedNode(start, PointSide.IN), direction);
        List<DirectedNode> shortestPath = routing.findShortestPath(start, direction, end);

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

        GraphToRoutingGraphMapper mapper = new GraphToRoutingGraphMapper(); // TODO consider height and electrification
        var routingGraph = mapper.mapGraphToJGraphT(graph);

        DirectedNode pointAIn = new DirectedNode(new GraphPoint(new PointName("A")), PointSide.IN);
        DirectedNode pointAOut = new DirectedNode(new GraphPoint(new PointName("A")), PointSide.OUT);
        DirectedNode pointBIn = new DirectedNode(new GraphPoint(new PointName("B")), PointSide.IN);
        DirectedNode pointBOut = new DirectedNode(new GraphPoint(new PointName("B")), PointSide.OUT);
        DirectedNode pointCIn = new DirectedNode(new GraphPoint(new PointName("C")), PointSide.IN);
        DirectedNode pointCOut = new DirectedNode(new GraphPoint(new PointName("C")), PointSide.OUT);
        DirectedNode pointZIn = new DirectedNode(new GraphPoint(new PointName("Z")), PointSide.IN);
        DirectedNode pointZOut = new DirectedNode(new GraphPoint(new PointName("Z")), PointSide.OUT);

        DefaultMonoTrainRoutingStrategy routing = new DefaultMonoTrainRoutingStrategy(routingGraph, RoutingAlgorithm.DIJKSTRA);
        List<DirectedNode> shortestPath = routing.findShortestPath(pointAIn, pointZIn);

        assertThat(shortestPath).isNotNull();
        assertThat(shortestPath).isEqualTo(List.of(pointAIn, pointBOut, pointCIn, pointBIn, pointAOut, pointZIn));
    }
}