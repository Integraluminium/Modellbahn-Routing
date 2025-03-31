package de.dhbw.modellbahn.adapter.locomotive.reading;

public record ConfigLocomotive(
        int id,
        String name,
        double maxSpeed,
        long accelerationTime,
        boolean electric,
        int accelerationDistance,
        long decelerationTime,
        int decelerationDistance,
        String position,
        String facingDirection
) {
}
