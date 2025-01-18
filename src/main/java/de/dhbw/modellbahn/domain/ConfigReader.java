package de.dhbw.modellbahn.domain;

import de.dhbw.modellbahn.adapter.api.ApiConfig;

import java.util.List;

public interface ConfigReader {
    List<Integer> getValidLocIds();
    ApiConfig getApiConfig();
}
