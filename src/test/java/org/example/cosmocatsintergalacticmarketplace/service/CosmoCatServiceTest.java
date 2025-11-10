package org.example.cosmocatsintergalacticmarketplace.service;

import org.example.cosmocatsintergalacticmarketplace.featuretoggle.exception.FeatureNotAvailableException;
import org.example.cosmocatsintergalacticmarketplace.featuretoggle.FeatureToggleProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CosmoCatServiceTest {

    @Autowired
    private CosmoCatService service;

    @Autowired
    private FeatureToggleProperties props;

    @BeforeEach
    void beforeEach() {
        props.getToggles().put("cosmo-cats", true);
        props.getToggles().put("kitty-products", false);
    }

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

    @Test
    void whenCosmoCatsDisabled_thenThrow() {
        props.getToggles().put("cosmo-cats", false);
        assertThrows(FeatureNotAvailableException.class, () -> service.getCosmoCats());
    }
}