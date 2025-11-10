package org.example.cosmocatsintergalacticmarketplace.service;
import org.example.cosmocatsintergalacticmarketplace.featuretoggle.FeatureToggleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class FeatureToggleServiceTest {

    private FeatureToggleService featureToggleService;

    @BeforeEach
    void setUp() {
        featureToggleService = new FeatureToggleService();
        ReflectionTestUtils.setField(featureToggleService, "cosmoCatsEnabled", true);
        ReflectionTestUtils.setField(featureToggleService, "kittyProductsEnabled", false);
    }

    @ParameterizedTest(name = "Feature \"{0}\" очікується {1}")
    @CsvSource({
            "feature.cosmoCats.enabled, true",
            "feature.kittyProducts.enabled, false",
            "feature.unknown.enabled, false"
    })
    void testIsFeatureEnabled(String featureName, boolean expected) {
        boolean result = featureToggleService.isFeatureEnabled(featureName);
        assertEquals(expected, result);
    }

    @ParameterizedTest(name = "init() має виконуватися лише один раз — тест {0}")
    @CsvSource({
            "feature.cosmoCats.enabled",
            "feature.kittyProducts.enabled"
    })
    void testInitCalledOnlyOnce(String featureName) {
        featureToggleService.isFeatureEnabled(featureName);

        ReflectionTestUtils.setField(featureToggleService, "cosmoCatsEnabled", false);
        ReflectionTestUtils.setField(featureToggleService, "kittyProductsEnabled", true);

        boolean result = featureToggleService.isFeatureEnabled(featureName);
        assertTrue(result == (featureName.equals("feature.cosmoCats.enabled")));
    }
}
