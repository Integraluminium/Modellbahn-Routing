package de.dhbw.modellbahn.adapter.graph_mapping;

public record ConfigThreeWaySwitch(
        String name,
        int id1,
        int id2,
        String root,
        String straight,
        String left,
        String right
) {
}
