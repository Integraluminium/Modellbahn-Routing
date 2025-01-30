package de.dhbw.modellbahn.adapter.graph_mapping;

public record ConfigCrossSwitch(
        String name,
        int id,
        String root1,
        String root2,
        String turnout1,
        String turnout2) {
}
