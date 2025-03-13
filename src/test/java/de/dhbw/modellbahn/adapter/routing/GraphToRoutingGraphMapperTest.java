package de.dhbw.modellbahn.adapter.routing;

import de.dhbw.modellbahn.adapter.moba.communication.ApiService;
import de.dhbw.modellbahn.adapter.moba.communication.calls.TrackComponentCallsAdapter;
import de.dhbw.modellbahn.adapter.track_generation.GraphGenerator;
import de.dhbw.modellbahn.application.port.moba.communication.TrackComponentCalls;
import de.dhbw.modellbahn.application.routing.DirectedNode;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.graph.PointName;
import de.dhbw.modellbahn.domain.graph.PointSide;
import de.dhbw.modellbahn.plugin.MockedConfigReader;
import de.dhbw.modellbahn.plugin.routing.jgrapht.mapper.GraphToRoutingGraphMapper;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class GraphToRoutingGraphMapperTest {

    @Test
    void mapGraph() {
        // Arrange
        GraphToRoutingGraphMapper mapper = new GraphToRoutingGraphMapper();

        ApiService apiService = new ApiService(0);
        TrackComponentCalls calls = new TrackComponentCallsAdapter(apiService);
        GraphGenerator generator = new GraphGenerator(new MockedConfigReader(), calls);

        Graph graph = generator.generateGraph();

        // Act
        org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> actualGraph = mapper.mapGraphToJGraphT(graph);
        Set<DirectedNode> actualVertices = actualGraph.vertexSet();
        Set<DirectedNode> expectedVertices = createNodesFromStrings(List.of("A", "B", "C", "D", "E", "F", "G"));

        actualGraph.edgesOf(new DirectedNode(GraphPoint.of("A"), PointSide.IN));

        // Assert
        assertThat(actualGraph).isNotNull();
        assertThat(actualVertices).isEqualTo(expectedVertices);


        Set<Map<DirectedNode, DirectedNode>> actualEdges = actualGraph.edgeSet().stream().map(edge -> Map.of(actualGraph.getEdgeSource(edge), actualGraph.getEdgeTarget(edge))).collect(Collectors.toSet());
        assertThat(actualEdges).isNotNull();

        Set<Map<DirectedNode, DirectedNode>> expectedEdges = generateExpectedEdges();
        assertThat(actualEdges).isEqualTo(expectedEdges);

    }

    private Set<Map<DirectedNode, DirectedNode>> generateExpectedEdges() {
        Set<Map<DirectedNode, DirectedNode>> expectedEdges = new HashSet<>();

        expectedEdges.add(Map.of(
                new DirectedNode(GraphPoint.of("A"), PointSide.IN),
                new DirectedNode(GraphPoint.of("B"), PointSide.IN)
        ));
        expectedEdges.add(Map.of(
                new DirectedNode(GraphPoint.of("A"), PointSide.OUT),
                new DirectedNode(GraphPoint.of("D"), PointSide.IN)
        ));
        expectedEdges.add(Map.of(
                new DirectedNode(GraphPoint.of("D"), PointSide.OUT),
                new DirectedNode(GraphPoint.of("A"), PointSide.IN)
        ));
        expectedEdges.add(Map.of(
                new DirectedNode(GraphPoint.of("A"), PointSide.IN),
                new DirectedNode(GraphPoint.of("E"), PointSide.OUT)
        ));
        expectedEdges.add(Map.of(
                new DirectedNode(GraphPoint.of("B"), PointSide.OUT),
                new DirectedNode(GraphPoint.of("A"), PointSide.OUT)
        ));
        expectedEdges.add(Map.of(
                new DirectedNode(GraphPoint.of("E"), PointSide.IN),
                new DirectedNode(GraphPoint.of("A"), PointSide.OUT)
        ));
        expectedEdges.add(Map.of(
                new DirectedNode(GraphPoint.of("B"), PointSide.IN),
                new DirectedNode(GraphPoint.of("C"), PointSide.IN)
        ));
        expectedEdges.add(Map.of(
                new DirectedNode(GraphPoint.of("C"), PointSide.OUT),
                new DirectedNode(GraphPoint.of("B"), PointSide.OUT)
        ));
        expectedEdges.add(Map.of(
                new DirectedNode(GraphPoint.of("G"), PointSide.OUT),
                new DirectedNode(GraphPoint.of("C"), PointSide.OUT)
        ));
        expectedEdges.add(Map.of(
                new DirectedNode(GraphPoint.of("C"), PointSide.IN),
                new DirectedNode(GraphPoint.of("G"), PointSide.IN)
        ));
        expectedEdges.add(Map.of(
                new DirectedNode(GraphPoint.of("F"), PointSide.OUT),
                new DirectedNode(GraphPoint.of("C"), PointSide.OUT)
        ));
        expectedEdges.add(Map.of(
                new DirectedNode(GraphPoint.of("C"), PointSide.IN),
                new DirectedNode(GraphPoint.of("F"), PointSide.IN)
        ));
        expectedEdges.add(Map.of(
                new DirectedNode(GraphPoint.of("F"), PointSide.IN),
                new DirectedNode(GraphPoint.of("G"), PointSide.OUT)
        ));
        expectedEdges.add(Map.of(
                new DirectedNode(GraphPoint.of("E"), PointSide.OUT),
                new DirectedNode(GraphPoint.of("D"), PointSide.OUT)
        ));
        expectedEdges.add(Map.of(
                new DirectedNode(GraphPoint.of("E"), PointSide.IN),
                new DirectedNode(GraphPoint.of("F"), PointSide.OUT)
        ));
        expectedEdges.add(Map.of(
                new DirectedNode(GraphPoint.of("D"), PointSide.IN),
                new DirectedNode(GraphPoint.of("E"), PointSide.IN)
        ));
        expectedEdges.add(Map.of(
                new DirectedNode(GraphPoint.of("E"), PointSide.IN),
                new DirectedNode(GraphPoint.of("A"), PointSide.OUT)
        ));
        expectedEdges.add(Map.of(
                new DirectedNode(GraphPoint.of("E"), PointSide.IN),
                new DirectedNode(GraphPoint.of("C"), PointSide.IN)
        ));
        expectedEdges.add(Map.of(
                new DirectedNode(GraphPoint.of("C"), PointSide.OUT),
                new DirectedNode(GraphPoint.of("E"), PointSide.OUT)
        ));
        expectedEdges.add(Map.of(
                new DirectedNode(GraphPoint.of("E"), PointSide.OUT),
                new DirectedNode(GraphPoint.of("D"), PointSide.OUT)
        ));
        expectedEdges.add(Map.of(
                new DirectedNode(GraphPoint.of("F"), PointSide.IN),
                new DirectedNode(GraphPoint.of("E"), PointSide.OUT)
        ));
        expectedEdges.add(Map.of(
                new DirectedNode(GraphPoint.of("G"), PointSide.IN),
                new DirectedNode(GraphPoint.of("F"), PointSide.OUT)
        ));

        return expectedEdges;
    }

    private Set<DirectedNode> createNodesFromStrings(List<String> names) {
        Set<DirectedNode> nodes = new HashSet<>();
        for (String name : names) {
            nodes.add(new DirectedNode(new GraphPoint(new PointName(name)), PointSide.IN));
            nodes.add(new DirectedNode(new GraphPoint(new PointName(name)), PointSide.OUT));
        }
        return nodes;
    }
}