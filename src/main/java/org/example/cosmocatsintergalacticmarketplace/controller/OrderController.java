package org.example.cosmocatsintergalacticmarketplace.controller;

import lombok.RequiredArgsConstructor;
import org.example.cosmocatsintergalacticmarketplace.dto.OrderDTO;
import org.example.cosmocatsintergalacticmarketplace.mapper.OrderMapper;
import org.example.cosmocatsintergalacticmarketplace.domain.Order;
import org.example.cosmocatsintergalacticmarketplace.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping
    public OrderDTO create(@RequestBody OrderDTO dto) {
        Order created = orderService.create(orderMapper.toDomain(dto));
        return orderMapper.toDTO(created);
    }

    @GetMapping
    public List<OrderDTO> getAll() {
        return orderService.findAll()
                .stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getById(@PathVariable Long id) {
        return orderService.findById(id)
                .map(orderMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        orderService.delete(id);
    }
}
