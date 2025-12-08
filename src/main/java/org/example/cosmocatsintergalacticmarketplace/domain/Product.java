package org.example.cosmocatsintergalacticmarketplace.domain;

import lombok.*;

import java.math.BigDecimal;

/**
 * DDD model: Product — reprsent goods on marketplace.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Long id;
    private String name;
    private BigDecimal price;
    private Category category;
    private String description;
    private Integer quantity;
}


