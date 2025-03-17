package de.dhbw.modellbahn.adapter.locomotive.reading;

public record ConfigLocomotive(
        int id,
        String name,
        double maxSpeed,
        long accelerationTime,
        int accelerationDistance,
        int decelerationDistance,
        String position,
        String facingDirection
) {
}
