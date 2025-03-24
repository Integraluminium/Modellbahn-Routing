package de.dhbw.modellbahn.parser;

import de.dhbw.modellbahn.application.repositories.LocomotiveRepository;
import de.dhbw.modellbahn.application.routing.building.RoutingAlgorithm;
import de.dhbw.modellbahn.application.routing.building.RoutingOptimization;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.attributes.LocId;


@SuppressWarnings("UseOfSystemOutOrSystemErr")
public class DomainObjectParser {


    private final Graph graph;
    private final LocomotiveRepository locomotiveRepository;


    public DomainObjectParser(Graph graph, LocomotiveRepository locomotiveRepository) {
        this.graph = graph;
        this.locomotiveRepository = locomotiveRepository;
    }

    public LocId parseLocId(final String locIdString) {
        int locIdInt = Integer.parseInt(locIdString);
        LocId locId = new LocId(locIdInt);
        if (!locomotiveRepository.existsLocomotive(locId)) {
            throw new IllegalArgumentException("Locomotive with id " + locId.id() + " is unknown.");
        }
        return locId;
    }

    public GraphPoint parseGraphPoint(final String graphPointString) {
        GraphPoint graphPoint = GraphPoint.of(graphPointString);
        if (!graph.contains(graphPoint)) {
            throw new IllegalArgumentException("GraphPoint " + graphPointString + " is not in the graph.");
        }
        return graphPoint;
    }

    public RoutingOptimization parseOptimization(final String optimizationString) {
        try {
            return RoutingOptimization.valueOf(optimizationString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Optimization " + optimizationString + " is unknown.");
        }
    }

    public RoutingAlgorithm parseRoutingAlgorithm(final String algorithmString) {
        try {
            return RoutingAlgorithm.valueOf(algorithmString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Algorithm " + algorithmString + " is unknown.");
        }
    }


}
