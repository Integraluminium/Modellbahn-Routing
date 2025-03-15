package de.dhbw.modellbahn.domain.track.components;

public enum SwitchState {
    STRAIGHT(1),
    DIVERGENT(0),
    UNKNOWN(-1);

    private final int value;

    SwitchState(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
