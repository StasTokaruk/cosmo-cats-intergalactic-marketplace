package org.example.cosmocatsintergalacticmarketplace.domain;

import lombok.*;

import java.util.List;

/**
 * DDD model: Customer`s orders, that contains list of product.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private List<Product> products;
    private Double totalPrice;
}