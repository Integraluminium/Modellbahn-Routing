package de.dhbw.modellbahn.domain.graph;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DistanceTest {
    private static Stream<Arguments> provideValidDistanceValues() {
        return Stream.of(
                Arguments.of(1),
                Arguments.of(50),
                Arguments.of(100)
        );
    }

    private static Stream<Arguments> provideInvalidDistanceValues() {
        return Stream.of(
                Arguments.of(0),
                Arguments.of(-10)
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidDistanceValues")
    void testValidSpeedValues(int input) {
        Distance distance = new Distance(input);

        assertEquals(input, distance.value());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidDistanceValues")
    void testInvalidSpeedValues(int input) {
        assertThrows(IllegalArgumentException.class, () -> new Distance(input));
    }
}