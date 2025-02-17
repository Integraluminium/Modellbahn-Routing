package de.dhbw.modellbahn.application.routing;

public class PathNotPossibleException extends Exception {
    public PathNotPossibleException(String message) {
        super(message);
    }

    public PathNotPossibleException() {
    }
}