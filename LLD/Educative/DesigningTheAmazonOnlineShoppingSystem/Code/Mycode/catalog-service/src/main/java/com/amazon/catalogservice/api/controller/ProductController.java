package com.amazon.catalogservice.api.controller;

import com.amazon.catalogservice.api.dto.ProductRequestDTO;
import com.amazon.catalogservice.api.dto.ProductResponseDTO;
import com.amazon.catalogservice.api.mapper.ProductMapper;
import com.amazon.catalogservice.domain.model.composite.ProductItem;
import com.amazon.catalogservice.domain.service.ProductService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // For fine-grained security
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products") // Production standard API versioning
public class ProductController {

    private final ProductService productService;

    // Spring DI ensures we use the ProductService (which depends on the LLD ProductRepository)
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // GET /api/v1/products - Public Access (permitAll in SecurityConfig)
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductItem> products = productService.getAllProducts();
        List<ProductResponseDTO> dtos = products.stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // GET /api/v1/products/{id} - Public Access
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        ProductItem product = productService.getProductById(id);
        return ResponseEntity.ok(ProductMapper.toDTO(product));
    }

    // POST /api/v1/products - Requires ADMIN role
    // @Valid triggers DTO validation (production standard)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Correct HTTP status for resource creation
    // Fine-grained security check, though already covered in SecurityConfig
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponseDTO createProduct(@Valid @RequestBody ProductRequestDTO requestDTO) {
        ProductItem productEntity = ProductMapper.toEntity(requestDTO);
        ProductItem savedProduct = productService.createOrUpdateProduct(productEntity);
        return ProductMapper.toDTO(savedProduct);
    }

    // PUT /api/v1/products/{id} - Requires ADMIN role
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO requestDTO) {

        // Production edge case handling: Ensure the ID is set correctly for update
        ProductItem existingProduct = productService.getProductById(id);
        ProductItem updatedEntity = ProductMapper.toEntity(requestDTO);
        updatedEntity.setId(existingProduct.getId()); // Ensure the ID from the path is used

        ProductItem savedProduct = productService.createOrUpdateProduct(updatedEntity);
        return ResponseEntity.ok(ProductMapper.toDTO(savedProduct));
    }

    // DELETE /api/v1/products/{id} - Requires ADMIN role
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Correct HTTP status for successful deletion
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}