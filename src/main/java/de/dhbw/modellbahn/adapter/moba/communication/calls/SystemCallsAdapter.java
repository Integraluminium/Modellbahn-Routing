package de.dhbw.modellbahn.adapter.moba.communication.calls;

import de.dhbw.modellbahn.adapter.moba.communication.ApiService;
import de.dhbw.modellbahn.adapter.moba.communication.dto.SystemGoCommand;
import de.dhbw.modellbahn.adapter.moba.communication.dto.SystemStopCommand;
import de.dhbw.modellbahn.application.port.moba.communication.SystemCalls;

public class SystemCallsAdapter implements SystemCalls {
    private final ApiService apiService;

    public SystemCallsAdapter(final ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void systemStop() {
        apiService.sendRequest("system/stop", new SystemStopCommand(
                apiService.getSenderHash(),
                true,
                0
        ));
    }

    @Override
    public void systemGo() {
        apiService.sendRequest("system/go", new SystemGoCommand(
                apiService.getSenderHash(),
                true,
                0
        ));
    }
}
