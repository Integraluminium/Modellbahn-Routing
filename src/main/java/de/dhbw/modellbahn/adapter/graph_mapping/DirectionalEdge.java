package de.dhbw.modellbahn.adapter.graph_mapping;

import de.dhbw.modellbahn.domain.graph.Distance;
import de.dhbw.modellbahn.domain.graph.GraphPoint;

import java.util.Objects;

public record DirectionalEdge(GraphPoint startPoint, GraphPoint destinationPoint, Distance distance) {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DirectionalEdge that = (DirectionalEdge) o;
        return Objects.equals(this.distance, that.distance()) && Objects.equals(this.startPoint, that.startPoint()) && Objects.equals(this.destinationPoint, that.destinationPoint());
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPoint, destinationPoint, distance);
    }
}
