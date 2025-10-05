package org.example.cosmocatsintergalacticmarketplace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private Long id;

    @NotBlank(message = "name must not be blank")
    private String name;

    @NotNull(message = "price must not be null")
    @Positive(message = "price must be positive")
    private Double price;

    @NotNull(message = "category must be provided")
    private CategoryDTO category;

    @NotBlank(message = "description must be provided")
    private String description;

    @NotNull(message = "must be in stock")
    @Positive(message = "quantity must be positive and in stock")
    private Double quantity;
}
