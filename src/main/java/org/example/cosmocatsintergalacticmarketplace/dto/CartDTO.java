package org.example.cosmocatsintergalacticmarketplace.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private Long id;
    private List<ProductDetailDTO> products;
    private Double totalPrice;
}
