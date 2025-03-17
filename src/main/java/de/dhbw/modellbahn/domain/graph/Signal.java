package de.dhbw.modellbahn.domain.graph;

import de.dhbw.modellbahn.domain.track.components.SignalComponent;

public class Signal extends GraphPoint {
    private final SignalComponent signalComponent;

    public Signal(PointName name, SignalComponent signalComponent) {
        super(name);
        this.signalComponent = signalComponent;
    }
}
