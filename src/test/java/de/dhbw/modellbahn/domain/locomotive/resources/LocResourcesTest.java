package de.dhbw.modellbahn.domain.locomotive.resources;

import de.dhbw.modellbahn.domain.locomotive.attributes.resources.FuelType;
import de.dhbw.modellbahn.domain.locomotive.attributes.resources.FuelValue;
import de.dhbw.modellbahn.domain.locomotive.attributes.resources.LocResources;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocResourcesTest {
    private final FuelValue fuelA = new FuelValue(0);
    private final FuelValue fuelB = new FuelValue(100);
    private final FuelValue sand = new FuelValue(255);

    @Test
    void testGetFuel() {
        LocResources locResources = new LocResources(fuelA, fuelB, sand);

        assertEquals(locResources.getFuel(FuelType.FUEL_A), fuelA);
        assertEquals(locResources.getFuel(FuelType.FUEL_B), fuelB);
        assertEquals(locResources.getFuel(FuelType.SAND), sand);
    }

    @Test
    void testSetFuel() {
        LocResources locResources = new LocResources(fuelA, fuelB, sand);
        FuelValue newFuelValue = new FuelValue(42);
        locResources.setFuel(FuelType.FUEL_A, newFuelValue);

        assertEquals(locResources.getFuel(FuelType.FUEL_A), newFuelValue);
    }
}