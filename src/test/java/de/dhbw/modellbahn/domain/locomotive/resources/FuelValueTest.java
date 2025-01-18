package de.dhbw.modellbahn.domain.locomotive.resources;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FuelValueTest {

    private static Stream<Arguments> provideValidFuelValues() {
        return Stream.of(
                Arguments.of(0),
                Arguments.of(50),
                Arguments.of(100),
                Arguments.of(255)
        );
    }

    private static Stream<Arguments> provideInvalidFuelValues() {
        return Stream.of(
                Arguments.of(256),
                Arguments.of(-1)
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidFuelValues")
    void testValidFuelValues(int input){
        FuelValue fuelValue = new FuelValue(input);

        assertEquals(input, fuelValue.value());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidFuelValues")
    void testInvalidFuelValues(int input){
        assertThrows(IllegalArgumentException.class, () -> new FuelValue(input));
    }
}