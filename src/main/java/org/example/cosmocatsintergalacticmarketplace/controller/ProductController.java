package org.example.cosmocatsintergalacticmarketplace.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.cosmocatsintergalacticmarketplace.domain.Product;
import org.example.cosmocatsintergalacticmarketplace.dto.ProductAvailabilityDTO;
import org.example.cosmocatsintergalacticmarketplace.dto.ProductDTO;
import org.example.cosmocatsintergalacticmarketplace.dto.ProductDetailDTO;
import org.example.cosmocatsintergalacticmarketplace.mapper.ProductMapper;
import org.example.cosmocatsintergalacticmarketplace.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping
    public ResponseEntity<ProductDTO> create(@Valid @RequestBody ProductDTO productDTO) {
        Product product = productMapper.toProductDomain(productDTO);
        Product saved_product = productService.create(product);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved_product.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(productMapper.toProductDTO(saved_product));
    }

    @GetMapping
    public List<ProductAvailabilityDTO> getAll() {
        return productService.findAll()
                .stream()
                .map(productMapper::toAvailabilityDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDTO> getById(@PathVariable Long id) {
        return productService.findById(id)
                .map(productMapper::toDetailDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@Valid @PathVariable Long id,
                                             @RequestBody ProductDTO productDTO) {
        Product product = productMapper.toProductDomain(productDTO);
        return productService.update(id, product)
                .map(productMapper::toProductDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}