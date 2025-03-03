package de.dhbw.modellbahn.domain.graph;

public enum PointSide {
    IN,
    OUT;

    public PointSide getOpposite() {
        return this == IN ? OUT : IN;
    }
}
