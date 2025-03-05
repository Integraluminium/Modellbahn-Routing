package de.dhbw.modellbahn.plugin.routing.jgrapht;

import de.dhbw.modellbahn.application.routing.DirectedNode;
import de.dhbw.modellbahn.application.routing.PathNotPossibleException;
import de.dhbw.modellbahn.domain.graph.GraphPoint;

import java.util.List;

/**
 * This interface is used to find the shortest path for a single train
 * <p>
 * All constraints like electrification must be adjusted in the graph before calling this method
 */
public interface MonoTrainRoutingStrategy {
    /**
     * Find the shortest for just one train
     * with <b>fixed</b> direction in destination
     *
     * @param start the start point for the Train
     * @param end   the end point for the Train
     * @return a list of edges that represent the shortest path
     */
    List<DirectedNode> findShortestPath(DirectedNode start, DirectedNode end) throws PathNotPossibleException;

    /**
     * Find the shortest for just one train
     * with <b>dynamic</b> direction in destination
     *
     * @param start       the start point for the Train
     * @param destination the destination for the Train
     * @return a list of edges that represent the shortest path
     */
    List<DirectedNode> findShortestPath(DirectedNode start, GraphPoint destination) throws PathNotPossibleException;

    List<DirectedNode> findShortestPath(GraphPoint start, GraphPoint facingDirection, GraphPoint end) throws PathNotPossibleException;
}
