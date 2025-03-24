package de.dhbw.modellbahn.adapter.physical.railway.communication.calls;

import de.dhbw.modellbahn.adapter.physical.railway.communication.ApiService;
import de.dhbw.modellbahn.adapter.physical.railway.communication.dto.SystemGoCommand;
import de.dhbw.modellbahn.adapter.physical.railway.communication.dto.SystemStopCommand;
import de.dhbw.modellbahn.domain.physical.railway.communication.SystemCalls;

public class SystemCallsAdapter implements SystemCalls {
    private final ApiService apiService;

    public SystemCallsAdapter(final ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void systemStop() {
        apiService.sendRequest("/system/stop", new SystemStopCommand(
                apiService.getSenderHash(),
                true,
                0
        ));
    }

    @Override
    public void systemGo() {
        apiService.sendRequest("/system/go", new SystemGoCommand(
                apiService.getSenderHash(),
                true,
                0
        ));
    }
}
