package org.example.cosmocatsintergalacticmarketplace.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class CosmoWordValidator implements ConstraintValidator<CosmoWordCheck, String> {

    private final List<String> cosmicWords = Arrays.asList("cosmo", "star", "galaxy", "comet");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return false;

        return cosmicWords.stream().anyMatch(word -> value.toLowerCase().contains(word));
    }
}
