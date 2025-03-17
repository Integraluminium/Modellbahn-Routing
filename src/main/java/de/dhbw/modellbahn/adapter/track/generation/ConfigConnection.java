package de.dhbw.modellbahn.adapter.track.generation;

public record ConfigConnection(
        String source,
        String destination,
        int distance,
        int height,
        boolean electrified
) {
}
