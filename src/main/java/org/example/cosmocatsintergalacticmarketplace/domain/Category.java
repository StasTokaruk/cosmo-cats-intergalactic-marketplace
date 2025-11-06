package org.example.cosmocatsintergalacticmarketplace.domain;

import lombok.*;

/**
 * DDD model: Category of product (like, Electronics, Books, etc.)
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    private Long id;
    private String name;
}
