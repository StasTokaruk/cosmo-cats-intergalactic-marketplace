package org.example.cosmocatsintergalacticmarketplace.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CartDTO {
    private Long id;
    private List<ProductDetailDTO> products;
    private BigDecimal totalPrice;
}
