package org.example.cosmocatsintergalacticmarketplace.featuretoggle.annotation;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FeatureToggleAnnotation {
    /**
     * Назва фічі (ключ у application.yml під application.feature.toggles).
     * Наприклад: "cosmo-cats" або "kitty-products"
     */
    String value();
}
