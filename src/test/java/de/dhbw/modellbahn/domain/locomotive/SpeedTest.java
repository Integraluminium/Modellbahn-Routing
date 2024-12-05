package de.dhbw.modellbahn.domain.locomotive;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SpeedTest {

    private static Stream<Arguments> provideValidSpeedValues() {
        return Stream.of(
                Arguments.of(0),
                Arguments.of(50),
                Arguments.of(100)
        );
    }

    private static Stream<Arguments> provideInvalidSpeedValues() {
        return Stream.of(
                Arguments.of(101),
                Arguments.of(-1)
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidSpeedValues")
    void testValidSpeedValues(int input){
        Speed speed = new Speed(input);

        assertEquals(input, speed.value());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidSpeedValues")
    void testInvalidSpeedValues(int input){
        assertThrows(IllegalArgumentException.class, () -> new Speed(input));
    }
}