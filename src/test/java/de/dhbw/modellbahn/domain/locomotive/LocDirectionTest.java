package de.dhbw.modellbahn.domain.locomotive;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LocDirectionTest {

    private static Stream<Arguments> provideValidDirections() {
        return Stream.of(
                Arguments.of(LocDirection.TOGGLE, "Toggle"),
                Arguments.of(LocDirection.FORWARDS, "Forwards"),
                Arguments.of(LocDirection.BACKWARDS, "Backwards"),
                Arguments.of(LocDirection.KEEP, "Keep")
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidDirections")
    void testValidDirections(LocDirection input, String expected){
        assertEquals(input.getDirection(), expected);
    }

}