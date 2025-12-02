package org.example.cosmocatsintergalacticmarketplace.service;

import org.example.cosmocatsintergalacticmarketplace.featuretoggle.exception.FeatureNotAvailableException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CosmoCatServiceEnabledTest {

    @DynamicPropertySource
    static void registerProps(DynamicPropertyRegistry registry) {
        registry.add("feature-toggles.cosmo-cats", () -> true);
        registry.add("feature-toggles.kitty-products", () -> false);
    }

    @Autowired
    private CosmoCatService service;

    @Test
    void whenCosmoCatsEnabled_thenReturnList() {
        var list = service.getCosmoCats();
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    void whenKittyProductsDisabled_thenThrow() {
        assertThrows(FeatureNotAvailableException.class, () -> service.getKittyProducts());
    }
}
