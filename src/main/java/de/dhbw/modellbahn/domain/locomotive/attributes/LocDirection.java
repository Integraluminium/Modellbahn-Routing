package de.dhbw.modellbahn.domain.locomotive.attributes;

public enum LocDirection {
    KEEP("Keep"),
    FORWARDS("Forwards"),
    BACKWARDS("Backwards"),
    TOGGLE("Toggle");

    private final String direction;

    LocDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }
}
