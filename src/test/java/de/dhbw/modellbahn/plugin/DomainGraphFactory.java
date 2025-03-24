package de.dhbw.modellbahn.plugin;

import de.dhbw.modellbahn.adapter.physical.railway.communication.calls.MockedTrackComponentCalls;
import de.dhbw.modellbahn.adapter.track.generation.GraphGenerator;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.physical.railway.communication.TrackComponentCalls;

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
