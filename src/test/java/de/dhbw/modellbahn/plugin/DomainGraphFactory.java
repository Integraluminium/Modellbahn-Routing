package de.dhbw.modellbahn.plugin;

import de.dhbw.modellbahn.adapter.moba.communication.calls.MockedTrackComponentCalls;
import de.dhbw.modellbahn.adapter.track.generation.GraphGenerator;
import de.dhbw.modellbahn.application.port.moba.communication.TrackComponentCalls;
import de.dhbw.modellbahn.domain.graph.Graph;

public class DomainGraphFactory {
    public static Graph createTestGraph() {
        TrackComponentCalls calls = new MockedTrackComponentCalls();
        GraphGenerator generator = new GraphGenerator(new MockedConfigReader(), calls);
        return generator.generateGraph();
    }

    public static Graph createSmallTestGraph() {
        TrackComponentCalls calls = new MockedTrackComponentCalls();
        GraphGenerator generator = new GraphGenerator(new MockedConfigReader_smallGraph(), calls);
        return generator.generateGraph();
    }
}
