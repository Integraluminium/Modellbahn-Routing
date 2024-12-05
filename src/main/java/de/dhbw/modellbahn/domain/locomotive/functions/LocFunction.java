package de.dhbw.modellbahn.domain.locomotive.functions;

public enum LocFunction {
    ;

    private final int functionID;

    LocFunction(int functionID) {
        this.functionID = functionID;
    }

    public int getFunctionID() {
        return functionID;
    }
}
