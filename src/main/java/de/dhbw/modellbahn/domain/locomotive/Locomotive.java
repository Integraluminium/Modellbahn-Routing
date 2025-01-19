package de.dhbw.modellbahn.domain.locomotive;

import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.resources.FuelType;
import de.dhbw.modellbahn.domain.locomotive.resources.FuelValue;
import de.dhbw.modellbahn.domain.locomotive.resources.LocResources;

public class Locomotive {
    private final LocId id;
    private LocResources resources;
    private GraphPoint currentPosition;
    private GraphPoint currentFacingDirection;
    private Speed currentSpeed;


    public Locomotive(LocId locId, GraphPoint startPosition, GraphPoint startFacingDirection) {
        this.id = locId;
        this.currentPosition = startPosition;
        this.currentFacingDirection = startFacingDirection;
        this.currentSpeed = new Speed(0);
    }

    public LocId getLocId() {
        return id;
    }

    private void setFuel(FuelType fuelType, FuelValue fuelValue) {

    }

    public FuelValue getFuel(FuelType fuelType) {
        return null;
    }

    public LocResources getAllFuels() {
        return null;
    }

    public GraphPoint getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(GraphPoint currentPosition) {
        this.currentPosition = currentPosition;
    }

    public GraphPoint getCurrentFacingDirection() {
        return currentFacingDirection;
    }

    public void setCurrentFacingDirection(GraphPoint currentFacingDirection) {
        this.currentFacingDirection = currentFacingDirection;
    }

    public Speed getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(Speed currentSpeed) {
        this.currentSpeed = currentSpeed;
    }
}
