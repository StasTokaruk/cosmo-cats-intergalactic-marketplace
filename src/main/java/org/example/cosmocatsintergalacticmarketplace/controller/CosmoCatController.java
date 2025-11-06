package org.example.cosmocatsintergalacticmarketplace.controller;


import org.example.cosmocatsintergalacticmarketplace.service.CosmoCatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CosmoCatController {

    private final CosmoCatService service;

    public CosmoCatController(CosmoCatService service) {
        this.service = service;
    }

    @GetMapping("/api/v1/cats")
    public ResponseEntity<List<String>> cats() {
        return ResponseEntity.ok(service.getCosmoCats());
    }

    @GetMapping("/api/v1/feature-products")
    public ResponseEntity<List<String>> products() {
        return ResponseEntity.ok(service.getKittyProducts());
    }
}

