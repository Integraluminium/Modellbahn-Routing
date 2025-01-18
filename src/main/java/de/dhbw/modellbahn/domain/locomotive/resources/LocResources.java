package de.dhbw.modellbahn.domain.locomotive.resources;

public class LocResources {
    private FuelValue fuelA;
    private FuelValue fuelB;
    private FuelValue sand;

    public LocResources(FuelValue fuelA, FuelValue fuelB, FuelValue sand) {
        this.fuelA = fuelA;
        this.fuelB = fuelB;
        this.sand = sand;
    }

    public void setFuel(FuelType fuelType, FuelValue fuelValue) {
        switch (fuelType) {
            case FuelType.FUEL_A -> this.fuelA = fuelValue;
            case FuelType.FUEL_B -> this.fuelB = fuelValue;
            case FuelType.SAND -> this.sand = fuelValue;
        }
    }

    public FuelValue getFuel(FuelType fuelType) {
        FuelValue fuel;
        switch (fuelType) {
            case FuelType.FUEL_A -> fuel = this.fuelA;
            case FuelType.FUEL_B -> fuel = this.fuelB;
            case FuelType.SAND -> fuel = this.sand;
            default -> throw new IllegalArgumentException("FuelType " + fuelType + "does not exist.");
        }
        return fuel;
    }
}
