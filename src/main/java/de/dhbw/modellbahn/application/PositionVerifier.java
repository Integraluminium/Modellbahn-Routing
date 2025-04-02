package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.application.routing.actions.PositionVerificationAction;
import de.dhbw.modellbahn.application.routing.actions.RoutingAction;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class PositionVerifier {
    private static final Logger logger = Logger.getLogger(PositionVerifier.class.getSimpleName());
    private final Map<Integer, GraphPoint> contactPointMap = new ConcurrentHashMap<>();

    public PositionVerifier() {
        // Initialize with track contact to graph point mappings
    }

    public void registerContactPoint(int contactId, GraphPoint point) {
        contactPointMap.put(contactId, point);
    }

    public Route enhanceRouteWithVerification(Route originalRoute) {
        List<RoutingAction> newActions = new ArrayList<>();
        List<RoutingAction> originalActions = originalRoute.getActionList();

        for (int i = 0; i < originalActions.size(); i++) {
            RoutingAction action = originalActions.get(i);
            newActions.add(action);

            // Add verification points at strategic positions
            // This is a simplified example - you'd need logic to determine
            // appropriate verification points based on your track layout
            if (i > 0 && i < originalActions.size() - 1 && i % 3 == 0) {
                GraphPoint expectedPoint = originalRoute.getLoc().getCurrentPosition();
                PositionVerificationAction verification =
                        new PositionVerificationAction(originalRoute.getLoc(), expectedPoint);
                newActions.add(verification);
            }
        }

        return new Route(
                originalRoute.getLoc(),
                newActions,
                originalRoute.getNewPosition(),
                originalRoute.getNewFacingDirection(),
                originalRoute.getEstimatedTime()
        );
    }

    public GraphPoint getPointForContact(int contactId) {
        return contactPointMap.get(contactId);
    }
}