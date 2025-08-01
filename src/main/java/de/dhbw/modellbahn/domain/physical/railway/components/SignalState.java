package de.dhbw.modellbahn.domain.physical.railway.components;

public enum SignalState {
    DANGER(0),
    CLEAR(1),
    UNKNOWN(-1);

    private final int value;

    SignalState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
