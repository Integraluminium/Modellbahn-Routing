package de.dhbw.modellbahn.domain;

import de.dhbw.modellbahn.adapter.api.ApiConfig;
import de.dhbw.modellbahn.adapter.graph_mapping.Connection;

import java.util.List;

public interface ConfigReader {
    List<Integer> getValidLocIds();
    ApiConfig getApiConfig();
    List<Connection> getConnections();
}
