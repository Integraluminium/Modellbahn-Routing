package de.dhbw.modellbahn.domain.trackcomponents;

public enum SwitchState {
    STRAIGHT(0),
    DIVERGENT(1),
    UNKNOWN(-1);

    private final int value;

    SwitchState(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
