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
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class GraphMapperTest {

    @Test
    void mapGraph() {
        // Arrange
        GraphMapper mapper = new GraphMapper();

        ApiService apiService = new ApiService(0);
        TrackComponentCalls calls = new TrackComponentCallsAdapter(apiService);
        GraphGenerator generator = new GraphGenerator(new MockedConfigReader(), calls);

        Graph graph = generator.generateGraph();

        // Act
        org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> actualGraph = mapper.mapGraph(graph);
        Set<DirectedNode> actualVertices = actualGraph.vertexSet();
        Set<DirectedNode> expectedVertices = createNodesFromStrings(List.of("A", "B", "C", "D", "E", "F", "G"));

        actualGraph.edgesOf(new DirectedNode(new GraphPoint(new PointName("A")), PointSide.IN));
//        System.out.println(actualGraph.edgesOf(new DirectedNode(new GraphPoint(new PointName("A")), PointSide.IN)));
//        System.out.println(actualGraph.edgesOf(new DirectedNode(new GraphPoint(new PointName("A")), PointSide.OUT)));
//
        for (var pointname : List.of("A", "B", "C", "D", "E", "F", "G")) {
            for (var side : List.of(PointSide.IN, PointSide.OUT)) {
                System.out.println("Point: " + pointname + " Side: " + side);
                for (var x : actualGraph.edgesOf(new DirectedNode(new GraphPoint(new PointName(pointname)), side))) {
                    System.out.println(x);
                }
            }
        }


        // Assert
        assertThat(actualGraph).isNotNull();
        assertThat(actualVertices).isEqualTo(expectedVertices);
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