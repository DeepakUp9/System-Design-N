package com.stackclonell.stackclone.service;

import com.stackclonell.stackclone.core.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service responsible for resolving feature flag status for a given user context.
 */
@Service
public class FeatureFlagService {

    @Value("${app.features.new-sorting-ui-enabled}")
    private boolean newSortingUiEnabledDefault;

    @Value("${app.features.kill-switch-comments}")
    private boolean killSwitchComments;

    // Simple example of a hardcoded beta list (Production uses external service lookups)
    private final String BETA_USERS = "admin,beta_tester1,beta_tester2";

    /**
     * Determines if the new sorting UI should be enabled for the user.
     * This simulates an A/B test with a 50/50 split.
     */
    public boolean isNewSortingUiEnabled(Optional<User> currentUser) {
        // If flag is explicitly set to false in config, respect it.
        if (!newSortingUiEnabledDefault) {
            return false;
        }

        // For A/B testing: Simple implementation of 50% split based on User ID parity
        if (currentUser.isPresent()) {
            // User ID is used as the randomization seed for consistency (Sticky A/B test)
            return currentUser.get().getId() % 2 == 0;
        }

        // Default to the configuration if no user context is available
        return newSortingUiEnabledDefault;
    }

    /**
     * Checks if a critical feature is disabled via a kill switch.
     * (Resilience feature)
     */
    public boolean isCommentsAllowed() {
        return !killSwitchComments;
    }

    /**
     * Checks if the user is part of the beta group.
     */
    public boolean isUserInBeta(String username) {
        return BETA_USERS.contains(username);
    }
}