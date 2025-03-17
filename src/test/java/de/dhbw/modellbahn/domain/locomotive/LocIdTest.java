package de.dhbw.modellbahn.domain.locomotive;

import de.dhbw.modellbahn.adapter.locomotive.LocomotiveRepositoryImpl;
import de.dhbw.modellbahn.adapter.moba.communication.calls.MockedLockCalls;
import de.dhbw.modellbahn.application.LocomotiveRepository;
import de.dhbw.modellbahn.application.port.moba.communication.LocCalls;
import de.dhbw.modellbahn.domain.ConfigReader;
import de.dhbw.modellbahn.plugin.YAMLConfigReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LocIdTest {
    private static ConfigReader configReader;
    private static LocomotiveRepository locomotiveRepository;

    @BeforeAll
    static void createConfigReader() {
        configReader = new YAMLConfigReader();
        LocCalls locCalls = new MockedLockCalls();
        locomotiveRepository = new LocomotiveRepositoryImpl(configReader, locCalls);
    }

    private static Stream<Arguments> provideValidLocIds() {
        return locomotiveRepository.getAvailableLocIds().stream().map(LocId::id).map(Arguments::of);
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
        LocId locId = new LocId(input);

        assertEquals(input, locId.id());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidLocIds")
    void testInvalidLocIds(int input) {
        if (input <= 0) {
            assertThrows(IllegalArgumentException.class, () -> new LocId(input));
        } else {
            assertFalse(locomotiveRepository.existsLocomotive(new LocId(input)));
        }
    }
}