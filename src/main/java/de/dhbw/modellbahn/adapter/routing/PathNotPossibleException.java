package de.dhbw.modellbahn.adapter.routing;

public class PathNotPossibleException extends Exception {
    public PathNotPossibleException(String message) {
        super(message);
    }

    public PathNotPossibleException() {
    }
}