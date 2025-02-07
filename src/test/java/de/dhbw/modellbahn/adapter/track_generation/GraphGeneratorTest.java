package de.dhbw.modellbahn.adapter.track_generation;

import de.dhbw.modellbahn.adapter.moba.communication.ApiService;
import de.dhbw.modellbahn.adapter.moba.communication.calls.TrackComponentCallsAdapter;
import de.dhbw.modellbahn.application.port.moba.communication.TrackComponentCalls;
import de.dhbw.modellbahn.domain.ConfigReader;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
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
        ConfigReader configReader = new MockedConfigReader();
        ApiService apiService = new ApiService(0);
        TrackComponentCalls trackComponentCalls = new TrackComponentCallsAdapter(apiService);
        GraphGenerator graphGenerator = new GraphGenerator(configReader, trackComponentCalls);

        Graph graph = graphGenerator.generateGraph();

        Set<GraphPoint> actualVertices = graph.getAllVertices();
        Set<String> actualVerticeNames = actualVertices.stream().map(GraphPoint::getName).collect(Collectors.toSet());
        Set<String> expectedVerticeNames = Set.of("A", "B", "C", "D", "E", "F", "G");
        assertEquals(actualVerticeNames, expectedVerticeNames);

        List<WeightedEdge> actualEdgesOfA = graph.getEdgesOfVertex("A");
        List<String> actualNamesOfEdgesOfA = actualEdgesOfA.stream().map(edge -> edge.destination().getName()).toList();
        List<String> expectedNamesOfEdgesOfA = List.of("B", "D", "E");
        assertThat(actualNamesOfEdgesOfA).containsExactlyInAnyOrderElementsOf(expectedNamesOfEdgesOfA);
    }
}