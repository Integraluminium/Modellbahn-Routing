package de.dhbw.modellbahn.adapter.graph_mapping;

public record Connection(
        String source,
        String destination,
        int distance,
        int height,
        boolean electrified
) {
}
