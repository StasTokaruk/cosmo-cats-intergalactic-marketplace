package org.example.cosmocatsintergalacticmarketplace.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.cosmocatsintergalacticmarketplace.validation.CosmoWordCheck;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailDTO {
    private Long id;

    @NotBlank(message = "name must not be blank")
    @CosmoWordCheck
    private String name;

    @Valid
    @NotNull(message = "category must be provided")
    private CategoryDTO category;

    @NotBlank(message = "description must be provided")
    private String description;
}
