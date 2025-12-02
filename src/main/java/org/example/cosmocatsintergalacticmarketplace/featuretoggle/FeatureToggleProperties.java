package org.example.cosmocatsintergalacticmarketplace.featuretoggle;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "application.feature")
public class FeatureToggleProperties {

    /**
     * Повністю читає application.feature.toggles.*
     * У YAML: application.feature.toggles.cosmo-cats: true
     */
    private Map<String, Boolean> toggles = new HashMap<>();

    public Map<String, Boolean> getToggles() {
        return toggles;
    }

    public void setToggles(Map<String, Boolean> toggles) {
        this.toggles = toggles;
    }

    public boolean isEnabled(String key) {
        return toggles.getOrDefault(key, false);
    }
}

