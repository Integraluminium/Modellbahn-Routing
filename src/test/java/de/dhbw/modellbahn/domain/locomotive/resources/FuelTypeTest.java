package de.dhbw.modellbahn.domain.locomotive.resources;

import de.dhbw.modellbahn.domain.locomotive.LocDirection;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FuelTypeTest {

    private static Stream<Arguments> provideValidFuelTypes() {
        return Stream.of(
                Arguments.of(FuelType.FUEL_A, 1),
                Arguments.of(FuelType.FUEL_B, 2),
                Arguments.of(FuelType.SAND, 3)
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidFuelTypes")
    void testValidDirections(FuelType input, int expected){
        assertEquals(input.getType(), expected);
    }

}