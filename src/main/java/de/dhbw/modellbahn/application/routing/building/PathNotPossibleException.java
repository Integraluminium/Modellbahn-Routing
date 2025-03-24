package de.dhbw.modellbahn.application.routing.building;

public class PathNotPossibleException extends Exception {
    /**
     * Path to the destination is not possible.
     * e.g. because destination is not reachable.
     */
    public PathNotPossibleException(String message) {
        super(message);
    }

    public PathNotPossibleException() {
    }
}