package de.dhbw.modellbahn;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class CsvReader {
    public static List<Integer> readIntegerList(String filepath) throws IOException {
        List<Integer> integers;
        try (Stream<String> lines = Files.lines(Paths.get(filepath))) {
            List<List<String>> records = lines.map(line -> Arrays.asList(line.split(","))).toList();
            integers = records.stream().flatMap(List::stream).map(Integer::valueOf).toList();
        }
        return integers;
    }
}
