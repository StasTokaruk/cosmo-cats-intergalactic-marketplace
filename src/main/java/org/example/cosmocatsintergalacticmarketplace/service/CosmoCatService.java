package org.example.cosmocatsintergalacticmarketplace.service;



import org.example.cosmocatsintergalacticmarketplace.featuretoggle.annotation.FeatureToggleAnnotation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CosmoCatService {

    @FeatureToggleAnnotation("cosmo-cats")
    public List<String> getCosmoCats() {
        return List.of("Luna", "Orion", "Nebula");
    }

    @FeatureToggleAnnotation("kitty-products")
    public List<String> getKittyProducts() {
        return List.of("Space Milk", "Zero-G Yarn", "Meteorite Scratch Post");
    }
}


