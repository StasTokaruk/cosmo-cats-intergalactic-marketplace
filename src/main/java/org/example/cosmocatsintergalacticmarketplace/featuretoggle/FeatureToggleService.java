package org.example.cosmocatsintergalacticmarketplace.featuretoggle;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class FeatureToggleService {

    @Value("${feature.cosmoCats.enabled:true}")
    private boolean cosmoCatsEnabled;

    @Value("${feature.kittyProducts.enabled:false}")
    private boolean kittyProductsEnabled;

    private Map<String, Boolean> featureMap;

    public FeatureToggleService() {
        featureMap = new HashMap<>();
    }

    private void init() {
        featureMap.put("feature.cosmoCats.enabled", cosmoCatsEnabled);
        featureMap.put("feature.kittyProducts.enabled", kittyProductsEnabled);
    }

    public boolean isFeatureEnabled(String featureName) {
        if (featureMap.isEmpty()) init();
        return featureMap.getOrDefault(featureName, false);
    }
}
