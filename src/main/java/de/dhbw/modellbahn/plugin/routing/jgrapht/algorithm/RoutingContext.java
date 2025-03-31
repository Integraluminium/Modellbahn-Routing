package de.dhbw.modellbahn.plugin.routing.jgrapht.algorithm;

import de.dhbw.modellbahn.application.routing.building.RoutingAlgorithm;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.plugin.routing.jgrapht.LocomotiveInfo;

import java.util.Map;

public record RoutingContext(
        Graph domainGraph,
        Map<Locomotive, LocomotiveInfo> locomotiveInfoMap,
        RoutingAlgorithm routingAlgorithm,
        boolean considerElectrification,
        boolean considerHeight
) {
}