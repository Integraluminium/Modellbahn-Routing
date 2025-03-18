package de.dhbw.modellbahn.plugin.routing.jgrapht.mapper;

import de.dhbw.modellbahn.application.routing.DirectedNode;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.plugin.DomainGraphFactory;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GraphToRoutingGraphMapperTest {
    private static Graph graph;

    @BeforeAll
    static void beforeAll() {
        graph = DomainGraphFactory.createTestGraph();
    }

    @Test
    void testElectrified() {
        org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> jGraph = new GraphToRoutingGraphMapper().mapGraphToJGraphT(graph, true, new ArrayList<>());
        assertThat(graph.getAllVertices().size()).isEqualTo(7);
        assertThat(jGraph.vertexSet().size()).isEqualTo(14);
        Optional<DirectedNode> nodeA = jGraph.vertexSet().stream().filter(vertex -> vertex.getPoint().getName().equals("A")).findFirst();
        assertThat(nodeA.isPresent()).isTrue();

        boolean isEInEdges = jGraph.edgesOf(nodeA.get()).stream().anyMatch(edge -> jGraph.getEdgeTarget(edge).getNodeName().equals("E"));
        assertThat(isEInEdges).isFalse();
    }
}