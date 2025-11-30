package org.example.cosmocatsintergalacticmarketplace.domain;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * DDD model: Cart (All orders of customer in list) + total price
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private Long id;
    private List<Product> products;
    private BigDecimal totalPrice;

}
