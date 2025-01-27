package de.dhbw.modellbahn.application.path;

public class PathNotPossibleException extends Exception {
    public PathNotPossibleException(String message) {
        super(message);
    }

    public PathNotPossibleException() {
    }
}
