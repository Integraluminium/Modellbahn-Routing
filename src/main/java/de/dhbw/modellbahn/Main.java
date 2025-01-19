package de.dhbw.modellbahn;

import de.dhbw.modellbahn.adapter.moba.communication.ApiService;
import de.dhbw.modellbahn.adapter.moba.communication.calls.SystemCallsAdapter;
import de.dhbw.modellbahn.application.port.moba.communication.SystemCalls;
import de.dhbw.modellbahn.domain.ConfigReader;
import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.plugin.JSONConfigReader;

public class Main {
    public static void main(String[] args) {

        ConfigReader configReader = new JSONConfigReader();
        LocId id = new LocId(16389, configReader);

        var apiService = new ApiService(0);

        SystemCalls adapter = new SystemCallsAdapter(apiService);
        adapter.systemStop();

    }

    public static int foo() {
        return 1;
    }
}