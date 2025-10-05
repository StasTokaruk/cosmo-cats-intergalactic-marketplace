package org.example.cosmocatsintergalacticmarketplace.controller;

import lombok.RequiredArgsConstructor;
import org.example.cosmocatsintergalacticmarketplace.domain.Product;
import org.example.cosmocatsintergalacticmarketplace.dto.ProductAvailabilityDTO;
import org.example.cosmocatsintergalacticmarketplace.dto.ProductDTO;
import org.example.cosmocatsintergalacticmarketplace.dto.ProductDetailDTO;
import org.example.cosmocatsintergalacticmarketplace.mapper.ProductMapper;
import org.example.cosmocatsintergalacticmarketplace.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    // --- CREATE ---
    @PostMapping
    public ProductDTO create(@RequestBody ProductDTO productDTO) {
        Product product = productMapper.toProductDomain(productDTO);
        Product saved_product = productService.create(product);
        return productMapper.toProductDTO(saved_product);
    }

    // --- GET ALL (коротка інфа: список) ---
    @GetMapping
    public List<ProductAvailabilityDTO> getAll() {
        return productService.findAll()
                .stream()
                .map(productMapper::toAvailabilityDTO)
                .collect(Collectors.toList());
    }

    // --- GET ONE (детальна інфа) ---
    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDTO> getById(@PathVariable Long id) {
        return productService.findById(id)
                .map(productMapper::toDetailDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- UPDATE ---
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id,
                                                   @RequestBody ProductDTO productDTO) {
        Product product = productMapper.toProductDomain(productDTO);
        return productService.update(id, product)
                .map(productMapper::toProductDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- DELETE ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return productService.delete(id) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }
}
