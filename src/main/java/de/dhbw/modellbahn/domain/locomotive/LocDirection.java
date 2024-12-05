package de.dhbw.modellbahn.domain.locomotive;

public enum LocDirection {
    KEEP("Keep"),
    FORWARDS("Forwards"),
    BACKWARDS("Backwards"),
    TOGGLE("Toggle");

    private final String direction;

    private LocDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }
}
