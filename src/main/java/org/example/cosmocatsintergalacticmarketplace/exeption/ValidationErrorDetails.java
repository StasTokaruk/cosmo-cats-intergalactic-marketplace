package org.example.cosmocatsintergalacticmarketplace.exeption;

public record ValidationErrorDetails(
        String field,
        String message
) {}
