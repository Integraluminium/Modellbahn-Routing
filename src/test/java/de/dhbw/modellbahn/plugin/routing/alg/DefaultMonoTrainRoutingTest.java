package de.dhbw.modellbahn.plugin.routing.alg;

import de.dhbw.modellbahn.adapter.moba.communication.ApiService;
import de.dhbw.modellbahn.adapter.moba.communication.calls.TrackComponentCallsAdapter;
import de.dhbw.modellbahn.adapter.routing.GraphMapper;
import de.dhbw.modellbahn.adapter.track_generation.GraphGenerator;
import de.dhbw.modellbahn.application.port.moba.communication.TrackComponentCalls;
import de.dhbw.modellbahn.application.routing.DirectedNode;
import de.dhbw.modellbahn.application.routing.PathNotPossibleException;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.graph.PointName;
import de.dhbw.modellbahn.domain.graph.PointSide;
import de.dhbw.modellbahn.plugin.MockedConfigReader;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static de.dhbw.modellbahn.GraphVisualisation.showRoutingGraph;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DefaultMonoTrainRoutingTest {

    private static org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> createTestGraph() {
        ApiService apiService = new ApiService(0);
        TrackComponentCalls calls = new TrackComponentCallsAdapter(apiService);
        GraphGenerator generator = new GraphGenerator(new MockedConfigReader(), calls);
        Graph graph = generator.generateGraph();

        GraphMapper mapper = new GraphMapper();
        return mapper.mapGraph(graph);
    }

    private static org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> createOldRoutingGraph() {
        org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> newGraph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        DirectedNode pointAIn = new DirectedNode(new GraphPoint(new PointName("A")), PointSide.IN);
        DirectedNode pointAOut = new DirectedNode(new GraphPoint(new PointName("A")), PointSide.OUT);
        DirectedNode pointBIn = new DirectedNode(new GraphPoint(new PointName("B")), PointSide.IN);
        DirectedNode pointBOut = new DirectedNode(new GraphPoint(new PointName("B")), PointSide.OUT);
        DirectedNode pointCIn = new DirectedNode(new GraphPoint(new PointName("C")), PointSide.IN);
        DirectedNode pointCOut = new DirectedNode(new GraphPoint(new PointName("C")), PointSide.OUT);
        DirectedNode pointZIn = new DirectedNode(new GraphPoint(new PointName("Z")), PointSide.IN);
        DirectedNode pointZOut = new DirectedNode(new GraphPoint(new PointName("Z")), PointSide.OUT);


        newGraph.addVertex(pointAIn);
        newGraph.addVertex(pointAOut);
        newGraph.addVertex(pointBIn);
        newGraph.addVertex(pointBOut);
        newGraph.addVertex(pointCIn);
        newGraph.addVertex(pointCOut);
        newGraph.addVertex(pointZIn);
        newGraph.addVertex(pointZOut);

        newGraph.addEdge(pointAIn, pointBOut);
        newGraph.addEdge(pointAOut, pointCIn);
        newGraph.addEdge(pointAOut, pointZIn);
        newGraph.addEdge(pointBIn, pointAOut);
        newGraph.addEdge(pointBOut, pointCIn);
        newGraph.addEdge(pointBOut, pointCOut);
        newGraph.addEdge(pointCIn, pointBIn);
        newGraph.addEdge(pointCOut, pointBIn);
        newGraph.addEdge(pointCOut, pointAIn);
        newGraph.addEdge(pointZOut, pointAIn);

        return newGraph;
    }

    public static void main(String[] args) {
        org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> oldRoutingGraph = createOldRoutingGraph();
        org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> testGraph = createTestGraph();

//        showRoutingGraph(oldRoutingGraph);
        showRoutingGraph(testGraph);

        try {
            DirectedNode eIn = new DirectedNode(new GraphPoint(new PointName("A")), PointSide.IN);
            DirectedNode cIn = new DirectedNode(new GraphPoint(new PointName("C")), PointSide.IN);

            DefaultMonoTrainRouting routing = new DefaultMonoTrainRouting(testGraph);
            List<DirectedNode> path = routing.findShortestPath(eIn, cIn).getVertexList();
            System.out.println(path);
        } catch (PathNotPossibleException e) {
            e.printStackTrace();
        }
    }

    static Stream<Arguments> provideRoutingInfo() {
        DirectedNode pointAIn = new DirectedNode(new GraphPoint(new PointName("A")), PointSide.IN);
        DirectedNode pointAOut = new DirectedNode(new GraphPoint(new PointName("A")), PointSide.OUT);
        DirectedNode pointBIn = new DirectedNode(new GraphPoint(new PointName("B")), PointSide.IN);
        DirectedNode pointBOut = new DirectedNode(new GraphPoint(new PointName("B")), PointSide.OUT);
        DirectedNode pointCIn = new DirectedNode(new GraphPoint(new PointName("C")), PointSide.IN);
        DirectedNode pointCOut = new DirectedNode(new GraphPoint(new PointName("C")), PointSide.OUT);
        DirectedNode pointDIn = new DirectedNode(new GraphPoint(new PointName("D")), PointSide.IN);
        DirectedNode pointDOut = new DirectedNode(new GraphPoint(new PointName("D")), PointSide.OUT);
        DirectedNode pointEIn = new DirectedNode(new GraphPoint(new PointName("E")), PointSide.IN);
        DirectedNode pointEOut = new DirectedNode(new GraphPoint(new PointName("E")), PointSide.OUT);
        DirectedNode pointFIn = new DirectedNode(new GraphPoint(new PointName("F")), PointSide.IN);
        DirectedNode pointFOut = new DirectedNode(new GraphPoint(new PointName("F")), PointSide.OUT);
        DirectedNode pointGIn = new DirectedNode(new GraphPoint(new PointName("G")), PointSide.IN);
        DirectedNode pointGOut = new DirectedNode(new GraphPoint(new PointName("G")), PointSide.OUT);

        return Stream.of(
                Arguments.of(pointAIn, pointCIn, List.of(pointAIn, pointBIn, pointCIn)),
                Arguments.of(pointEOut, pointFOut, List.of(pointEOut, pointDOut, pointAIn, pointBIn, pointCIn, pointGIn, pointFOut)),
                Arguments.of(pointGIn, pointAIn, List.of(pointGIn, pointFOut, pointCOut, pointEOut, pointDOut, pointAIn)),
                Arguments.of(pointAIn, pointAOut, List.of(pointAIn, pointBIn, pointCIn, pointFIn, pointGOut, pointCOut, pointBOut, pointAOut))
        );
    }

    @Test
    void findShortestPath() throws PathNotPossibleException {
        // Arrange
        org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> routingGraph = createTestGraph();

        // Action
        GraphPoint start = new GraphPoint(new PointName("A"));
        GraphPoint direction = new GraphPoint(new PointName("D"));
        GraphPoint end = new GraphPoint(new PointName("G"));

        DefaultMonoTrainRouting routing = new DefaultMonoTrainRouting(routingGraph);
        var x = routing.findShortestPath(start, direction, end);
        assertThat(true).isTrue();

        // Assert
        for (DirectedNode directedNode : x) {
            System.out.println(directedNode.getNodeName());
        }
        assertThat(x).isNotNull();

    }

    @Test
    void testIsRoutingWorkingAtAll() throws PathNotPossibleException {
        org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> graph = createOldRoutingGraph();

        DirectedNode pointAIn = new DirectedNode(new GraphPoint(new PointName("A")), PointSide.IN);
        DirectedNode pointAOut = new DirectedNode(new GraphPoint(new PointName("A")), PointSide.OUT);
        DirectedNode pointBIn = new DirectedNode(new GraphPoint(new PointName("B")), PointSide.IN);
        DirectedNode pointBOut = new DirectedNode(new GraphPoint(new PointName("B")), PointSide.OUT);
        DirectedNode pointCIn = new DirectedNode(new GraphPoint(new PointName("C")), PointSide.IN);
        DirectedNode pointCOut = new DirectedNode(new GraphPoint(new PointName("C")), PointSide.OUT);
        DirectedNode pointZIn = new DirectedNode(new GraphPoint(new PointName("Z")), PointSide.IN);
        DirectedNode pointZOut = new DirectedNode(new GraphPoint(new PointName("Z")), PointSide.OUT);

        DefaultMonoTrainRouting routing = new DefaultMonoTrainRouting(graph);
        List<DirectedNode> shortestPath = routing.findShortestPath(pointAIn, pointZIn).getVertexList();

        assertThat(shortestPath).isNotNull();
        assertThat(shortestPath).isEqualTo(List.of(pointAIn, pointBOut, pointCIn, pointBIn, pointAOut, pointZIn));
    }

    @Test
    void testIsRoutingWorkingAtAll2() throws PathNotPossibleException {
        // setup
        org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> graph = createOldRoutingGraph();

        DirectedNode pointAIn = new DirectedNode(new GraphPoint(new PointName("A")), PointSide.IN);
        DirectedNode pointBOut = new DirectedNode(new GraphPoint(new PointName("B")), PointSide.OUT);
        DirectedNode pointCIn = new DirectedNode(new GraphPoint(new PointName("C")), PointSide.IN);

        // action
        DefaultMonoTrainRouting routing = new DefaultMonoTrainRouting(graph);
        List<DirectedNode> shortestPath = routing.findShortestPath(pointAIn, pointCIn).getVertexList();

        // assert
        assertThat(shortestPath).isNotNull();
        assertThat(shortestPath).isEqualTo(List.of(pointAIn, pointBOut, pointCIn));
    }

    @Test
    void findShortestPath2() throws PathNotPossibleException {
        org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> graph = createTestGraph();

        DirectedNode eIn = new DirectedNode(new GraphPoint(new PointName("E")), PointSide.IN);
        DirectedNode cIn = new DirectedNode(new GraphPoint(new PointName("C")), PointSide.IN);

        DefaultMonoTrainRouting routing = new DefaultMonoTrainRouting(graph);
        GraphPath<DirectedNode, DefaultWeightedEdge> graphPath = routing.findShortestPath(eIn, cIn);
        List<DirectedNode> path = graphPath.getVertexList();


        assertThat(path).isNotNull();
        assertThat(path).isEqualTo(List.of(eIn, cIn));
        assertThat(graphPath.getWeight()).isEqualTo(40.0);

    }

    @ParameterizedTest
    @MethodSource("provideRoutingInfo")
    void findShortestPath3(DirectedNode source, DirectedNode destination, List<DirectedNode> expected) {
        // Arrange
        org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> graph = createTestGraph();

        // Action
        DefaultMonoTrainRouting routing = new DefaultMonoTrainRouting(graph);
        List<DirectedNode> path = null;
        try {
            path = routing.findShortestPath(source, destination).getVertexList();
        } catch (PathNotPossibleException e) {
            e.printStackTrace();
        }

        // Assert
        assertThat(path).isNotNull();
        assertThat(path).isEqualTo(expected);
    }
}