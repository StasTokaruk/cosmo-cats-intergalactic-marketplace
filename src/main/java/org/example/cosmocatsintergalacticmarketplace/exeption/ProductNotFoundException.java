package org.example.cosmocatsintergalacticmarketplace.exeption;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product with id=" + id + " not found");
    }
}