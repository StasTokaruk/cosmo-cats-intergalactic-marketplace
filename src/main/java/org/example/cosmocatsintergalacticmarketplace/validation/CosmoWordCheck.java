package org.example.cosmocatsintergalacticmarketplace.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CosmoWordValidator.class) // клас валідатора
@Target({ElementType.FIELD}) // можна застосовувати до полів
@Retention(RetentionPolicy.RUNTIME)
public @interface CosmoWordCheck {
    String message() default "Product name must contain a cosmic word (cosmo, star, galaxy, comet)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
