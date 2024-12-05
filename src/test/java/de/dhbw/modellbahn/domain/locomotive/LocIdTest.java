package de.dhbw.modellbahn.domain.locomotive;

import de.dhbw.modellbahn.CsvReader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LocIdTest {

    public static Stream<Arguments> provideValidLocIds() throws IOException {
        String projectDir = System.getProperty("user.dir");
        String csvPath = "/src/main/java/de/dhbw/modellbahn/domain/locomotive/LocIds.csv";
        List<Integer> ids = CsvReader.readIntegerList(projectDir + csvPath);
        return ids.stream().map(Arguments::of);
    }
    public static Stream<Arguments> provideInvalidLocIds() {
        return Stream.of(
                Arguments.of(-120),
                Arguments.of(0),
                Arguments.of(100)
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidLocIds")
    void testValidLocIds(int input){
        LocId locId = new LocId(input);

        assertEquals(input, locId.id());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidLocIds")
    void testInvalidLocIds(int input){
        assertThrows(IllegalArgumentException.class, () -> new LocId(input));
    }
}