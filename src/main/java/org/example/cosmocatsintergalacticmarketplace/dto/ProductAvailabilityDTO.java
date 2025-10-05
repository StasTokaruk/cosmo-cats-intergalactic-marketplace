package org.example.cosmocatsintergalacticmarketplace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.example.cosmocatsintergalacticmarketplace.validation.CosmoWordCheck;

@Getter
@AllArgsConstructor
@Builder
public class ProductAvailabilityDTO {
    private Long id;

    @NotBlank(message = "name must not be blank")
    @CosmoWordCheck
    private String name;

    @NotNull(message = "must be in stock")
    @Positive(message = "quantity must be positive and in stock")
    private Double quantity;
}


