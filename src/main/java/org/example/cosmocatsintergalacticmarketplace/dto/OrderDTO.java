package org.example.cosmocatsintergalacticmarketplace.dto;

import lombok.*;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private List<ProductDetailDTO> products;
    private Double totalPrice;
}
