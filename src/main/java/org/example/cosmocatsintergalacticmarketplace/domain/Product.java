package org.example.cosmocatsintergalacticmarketplace.domain;

import lombok.*;

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
    private Double price;
    private Category category;
    private String description;
    private Double quantity;
}


