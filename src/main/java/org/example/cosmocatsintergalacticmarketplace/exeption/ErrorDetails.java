package org.example.cosmocatsintergalacticmarketplace.exeption;

public record ErrorDetails(
        int status,
        String error,
        String message,
        String path
) {}
