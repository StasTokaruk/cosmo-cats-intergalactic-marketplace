package org.example.cosmocatsintergalacticmarketplace.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.example.cosmocatsintergalacticmarketplace.validation.CosmoWordCheck;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private Long id;

    @NotBlank(message = "name must not be blank")
    @CosmoWordCheck
    private String name;

    @NotNull(message = "price must not be null")
    @Positive(message = "price must be positive")
    private Double price;

    @Valid
    @NotNull(message = "category must be provided")
    private CategoryDTO category;

    @NotBlank(message = "description must be provided")
    private String description;

    @NotNull(message = "must be in stock")
    @Positive(message = "quantity must be positive and in stock")
    private Double quantity;
}
