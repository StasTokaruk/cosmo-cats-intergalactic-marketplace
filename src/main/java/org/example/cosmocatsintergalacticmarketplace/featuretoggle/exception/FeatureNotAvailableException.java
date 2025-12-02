package org.example.cosmocatsintergalacticmarketplace.featuretoggle.exception;

public class FeatureNotAvailableException extends RuntimeException {

    public FeatureNotAvailableException(String featureName) {
        super(String.format("Feature '%s' is currently disabled.", featureName));
    }
}
