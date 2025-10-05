package org.example.cosmocatsintergalacticmarketplace.service;

import org.example.cosmocatsintergalacticmarketplace.domain.Cart;

import java.util.List;
import java.util.Optional;

public interface CartService {
    Cart create(Cart cart);
    List<Cart> findAll();
    Optional<Cart> findById(Long id);
    Optional<Cart> update(Long id, Cart cart);
    boolean delete(Long id);
}
