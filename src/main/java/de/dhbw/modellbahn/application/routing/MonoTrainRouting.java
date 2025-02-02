package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.adapter.routing.PathNotPossibleException;
import de.dhbw.modellbahn.adapter.routing.directional_graph.DirectedNode;

import java.util.List;

public interface MonoTrainRouting {
    // the directed graph to search in, must be already mapped with all constraints, like electrification

    /**
     * Find the shortest for just one train
     *
     * @param start the start point for the Train
     * @param end   the end point for the Train
     * @return a list of edges that represent the shortest path
     */
    List<DirectedNode> findShortestPath(DirectedNode start, DirectedNode end) throws PathNotPossibleException;
}
