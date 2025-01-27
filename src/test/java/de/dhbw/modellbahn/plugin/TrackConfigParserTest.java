package de.dhbw.modellbahn.plugin;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class TrackConfigParserTest {
    private static final String inputData = """
            switches:
              A:
                straight: C.straight
                diverging: Z.cn1
                root: B.root
              B:
                straight: C.root
                diverging: C.diverging
                root: A.root
              C:
                straight: A.straight
                diverging: B.diverging
                root: B.straight
            
            endpoints:
              Z:
                cn1: A.diverging
            """;

    @Test
    void testValidTestcase2() {
        Map<String, List<String>> nodesWithNeighbours = new TrackConfigParser(inputData).getDirectedNodesWithNeighbours();
        assertThat(nodesWithNeighbours).isNotNull();
        assertThat(nodesWithNeighbours).containsKeys("As", "Aw", "Bs", "Bw", "Cs", "Cw", "Zw");

        assertThat(nodesWithNeighbours.get("As")).containsExactly("Bw");
        assertThat(nodesWithNeighbours.get("Aw")).containsExactly("Cs", "Zs");

        assertThat(nodesWithNeighbours.get("Bs")).containsExactly("Aw");
        assertThat(nodesWithNeighbours.get("Bw")).containsExactly("Cs", "Cw");

        assertThat(nodesWithNeighbours.get("Cs")).containsExactly("Bs");
        assertThat(nodesWithNeighbours.get("Cw")).containsExactly("Bs", "As");

        assertThat(nodesWithNeighbours.get("Zw")).containsExactly("As");
        assertThat(nodesWithNeighbours.get("Zs")).isEmpty();
    }

    @Test
    void testPrinting() {
        TrackConfigParser parser = new TrackConfigParser(inputData);
        String result = parser.convertToString();

        assertThat(result).isEqualTo("""
                As->Bw
                Aw->Cs
                Aw->Zs
                Bs->Aw
                Bw->Cs
                Bw->Cw
                Cs->Bs
                Cw->Bs
                Cw->As
                Zw->As
                """);
    }

    @Test
    void getDirectedNodesWithNeighbours() {
        TrackConfigParser parser = new TrackConfigParser(inputData);
        var directedNodes = parser.getDirectedNodesWithNeighbours();

        assertThat(directedNodes).isNotNull();
        assertThat(directedNodes).containsKeys("As", "Aw", "Bs", "Bw", "Cs", "Cw", "Zw");
        assertThat(directedNodes.get("As")).containsExactly("Bw");
        assertThat(directedNodes.get("Aw")).containsExactly("Cs", "Zs");
        assertThat(directedNodes.get("Bs")).containsExactly("Aw");
        assertThat(directedNodes.get("Bw")).containsExactly("Cs", "Cw");
        assertThat(directedNodes.get("Cs")).containsExactly("Bs");
        assertThat(directedNodes.get("Cw")).containsExactly("Bs", "As");
        assertThat(directedNodes.get("Zw")).containsExactly("As");
    }
}