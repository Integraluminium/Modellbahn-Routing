package de.dhbw.modellbahn.domain.locomotive;

import de.dhbw.modellbahn.domain.ConfigReader;
import de.dhbw.modellbahn.plugin.YAMLConfigReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocIdTest {
    private static ConfigReader configReader;

    @BeforeAll
    static void createConfigReader() {
        configReader = new YAMLConfigReader();
    }

    private static Stream<Arguments> provideValidLocIds() {
        List<Integer> ids = configReader.getValidLocIds();
        return ids.stream().map(Arguments::of);
    }

    private static Stream<Arguments> provideInvalidLocIds() {
        return Stream.of(
                Arguments.of(-120),
                Arguments.of(0),
                Arguments.of(100)
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidLocIds")
    void testValidLocIds(int input) {
        LocId locId = new LocId(input, configReader);

        assertEquals(input, locId.id());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidLocIds")
    void testInvalidLocIds(int input) {
        assertThrows(IllegalArgumentException.class, () -> new LocId(input, configReader));
    }
}