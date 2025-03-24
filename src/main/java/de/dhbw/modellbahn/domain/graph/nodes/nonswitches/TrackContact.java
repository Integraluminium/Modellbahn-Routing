package de.dhbw.modellbahn.domain.graph.nodes.nonswitches;

import de.dhbw.modellbahn.domain.graph.nodes.attributes.PointName;
import de.dhbw.modellbahn.domain.physical.railway.components.TrackContactComponent;

public class TrackContact extends GraphPoint {
    private final TrackContactComponent trackContactComponent;

    public TrackContact(PointName name, TrackContactComponent trackContactComponent) {
        super(name);
        this.trackContactComponent = trackContactComponent;
    }
}
