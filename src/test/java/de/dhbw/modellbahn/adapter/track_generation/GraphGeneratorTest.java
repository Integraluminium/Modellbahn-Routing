package de.dhbw.modellbahn.adapter.track_generation;

import de.dhbw.modellbahn.adapter.moba.communication.ApiService;
import de.dhbw.modellbahn.adapter.moba.communication.calls.TrackComponentCallsAdapter;
import de.dhbw.modellbahn.application.port.moba.communication.TrackComponentCalls;
import de.dhbw.modellbahn.domain.ConfigReader;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.graph.PointName;
import de.dhbw.modellbahn.domain.graph.WeightedEdge;
import de.dhbw.modellbahn.plugin.MockedConfigReader;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class GraphGeneratorTest {

    @Test
    void testGenerateGraph() {
        // Generated graph of MockedConfigReader can be seen in plugin/MockedGraph.png
        ConfigReader configReader = new MockedConfigReader();
        ApiService apiService = new ApiService(0);
        TrackComponentCalls trackComponentCalls = new TrackComponentCallsAdapter(apiService);
        GraphGenerator graphGenerator = new GraphGenerator(configReader, trackComponentCalls);

        Graph graph = graphGenerator.generateGraph();

        Set<GraphPoint> actualVertices = graph.getAllVertices();
        Set<String> actualVerticeNames = actualVertices.stream().map(point -> point.getName().name()).collect(Collectors.toSet());
        Set<String> expectedVerticeNames = Set.of("A", "B", "C", "D", "E", "F", "G");
        assertEquals(actualVerticeNames, expectedVerticeNames);

        testEdgesOfVertex(graph, "A", List.of("B", "D", "E"));
        testEdgesOfVertex(graph, "B", List.of("A", "C"));
        testEdgesOfVertex(graph, "C", List.of("B", "E", "G", "F"));
        testEdgesOfVertex(graph, "D", List.of("A", "E"));
        testEdgesOfVertex(graph, "E", List.of("A", "C", "D", "F"));
        testEdgesOfVertex(graph, "F", List.of("C", "G", "E"));
        testEdgesOfVertex(graph, "G", List.of("C", "F"));
    }

    private void testEdgesOfVertex(Graph graph, String vertexName, List<String> expectedNamesOfEdges) {
        List<WeightedEdge> actualEdges = graph.getEdgesOfVertex(new PointName(vertexName));
        List<String> actualNamesOfEdges = actualEdges.stream().map(edge -> edge.destination().getName().name()).toList();
        assertThat(actualNamesOfEdges).containsExactlyInAnyOrderElementsOf(expectedNamesOfEdges);
    }
}