package de.dhbw.modellbahn.domain.locomotive;

import de.dhbw.modellbahn.domain.graph.Distance;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.resources.FuelType;
import de.dhbw.modellbahn.domain.locomotive.resources.FuelValue;
import de.dhbw.modellbahn.domain.locomotive.resources.LocResources;

public class Locomotive {
    private final LocName name;
    private final LocId id;
    private final MaxLocSpeed maxSpeed;
    private final long accelerationTime;
    private final Distance accelerationDistance;
    private final Distance decelerationDistance;
    private LocResources resources;
    private GraphPoint currentPosition;
    private GraphPoint currentFacingDirection;
    private Speed currentSpeed;


    public Locomotive(LocName locName, LocId locId, MaxLocSpeed maxSpeed, long accelerationTime, Distance accelerationDistance, Distance decelerationDistance, GraphPoint startPosition, GraphPoint startFacingDirection) {
        this.name = locName;
        this.id = locId;
        this.maxSpeed = maxSpeed;
        this.accelerationTime = accelerationTime;
        this.accelerationDistance = accelerationDistance;
        this.decelerationDistance = decelerationDistance;
        this.currentPosition = startPosition;
        this.currentFacingDirection = startFacingDirection;
        this.currentSpeed = new Speed(0);
    }

    public LocId getLocId() {
        return id;
    }

    public LocName getName() {
        return name;
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

    public Distance getAccelerationDistance() {
        return accelerationDistance;
    }

    public Distance getDecelerationDistance() {
        return decelerationDistance;
    }

    public MaxLocSpeed getMaxSpeed() {
        return maxSpeed;
    }

    public long getAccelerationTime() {
        return accelerationTime;
    }
}
