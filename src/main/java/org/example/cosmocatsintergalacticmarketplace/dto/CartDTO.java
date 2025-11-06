package org.example.cosmocatsintergalacticmarketplace.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CartDTO {
    private Long id;

    @NotNull(message = "Products list must not be null")
    @Valid
    private List<ProductDetailDTO> products;

    @NotNull(message = "Total price must be provided")
    @PositiveOrZero(message = "Total price must be non-negative")
    private Double totalPrice;
}
