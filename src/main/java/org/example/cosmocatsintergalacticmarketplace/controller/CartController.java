package org.example.cosmocatsintergalacticmarketplace.controller;

import lombok.RequiredArgsConstructor;
import org.example.cosmocatsintergalacticmarketplace.dto.CartDTO;
import org.example.cosmocatsintergalacticmarketplace.mapper.CartMapper;
import org.example.cosmocatsintergalacticmarketplace.domain.Cart;
import org.example.cosmocatsintergalacticmarketplace.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;

    @PostMapping
    public CartDTO create(@RequestBody CartDTO dto) {
        Cart created = cartService.create(cartMapper.toDomain(dto));
        return cartMapper.toDTO(created);
    }

    @GetMapping
    public List<CartDTO> getAll() {
        return cartService.findAll()
                .stream()
                .map(cartMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDTO> getById(@PathVariable Long id) {
        return cartService.findById(id)
                .map(cartMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        cartService.delete(id);
    }
}
