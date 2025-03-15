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
import de.dhbw.modellbahn.domain.graph.PointSide;
import de.dhbw.modellbahn.plugin.MockedConfigReader;
import de.dhbw.modellbahn.plugin.MockedConfigReader_smallGraph;
import de.dhbw.modellbahn.plugin.routing.jgrapht.MonoTrainRoutingStrategy;
import de.dhbw.modellbahn.plugin.routing.jgrapht.mapper.GraphToRoutingGraphMapper;
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

    @Test
    void findShortestPath() throws PathNotPossibleException {
        // Arrange
        Graph graph = createTestGraph();

        // Action
        GraphPoint start = GraphPoint.of("A");
        GraphPoint direction = GraphPoint.of("D");
        GraphPoint end = GraphPoint.of("G");

        GraphToRoutingGraphMapper mapper = new GraphToRoutingGraphMapper(); // TODO consider height and electrification
        var routingGraph = mapper.mapGraphToJGraphT(graph);

        MonoTrainRoutingStrategy routing = new DefaultMonoTrainRoutingStrategy(routingGraph, RoutingAlgorithm.DIJKSTRA);
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

        DirectedNode pointAIn = new DirectedNode(GraphPoint.of("A"), PointSide.IN);
        DirectedNode pointAOut = new DirectedNode(GraphPoint.of("A"), PointSide.OUT);
        DirectedNode pointBIn = new DirectedNode(GraphPoint.of("B"), PointSide.IN);
        DirectedNode pointBOut = new DirectedNode(GraphPoint.of("B"), PointSide.OUT);
        DirectedNode pointCIn = new DirectedNode(GraphPoint.of("C"), PointSide.IN);
        DirectedNode pointZIn = new DirectedNode(GraphPoint.of("Z"), PointSide.IN);

        MonoTrainRoutingStrategy routing = new DefaultMonoTrainRoutingStrategy(routingGraph, RoutingAlgorithm.DIJKSTRA);
        List<DirectedNode> shortestPath = routing.findShortestPath(pointAIn, pointZIn);

        assertThat(shortestPath).isNotNull();
        assertThat(shortestPath).isEqualTo(List.of(pointAIn, pointBOut, pointCIn, pointBIn, pointAOut, pointZIn));
    }
}