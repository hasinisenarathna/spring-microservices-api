package com.hasini.productservice.service.impl;

import com.hasini.productservice.dto.ProductRequestDTO;
import com.hasini.productservice.dto.ProductResponseDTO;
import com.hasini.productservice.model.Product;
import com.hasini.productservice.repository.ProductRepository;
import com.hasini.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor  // Lombok: generates constructor for all final fields
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    // ─── CREATE ───────────────────────────────────────────
    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {

        // 1. Convert DTO → Entity
        Product product = Product.builder()
                .name(requestDTO.getName())
                .description(requestDTO.getDescription())
                .price(requestDTO.getPrice())
                .stockQuantity(requestDTO.getStockQuantity())
                .build();

        // 2. Save to database
        Product savedProduct = productRepository.save(product);

        // 3. Convert saved Entity → ResponseDTO and return
        return mapToResponseDTO(savedProduct);
    }

    // ─── GET ALL ───────────────────────────────────────────
    @Override
    public List<ProductResponseDTO> getAllProducts() {

        return productRepository.findAll()  // returns List<Product>
                .stream()                   // stream each product
                .map(this::mapToResponseDTO)// convert each to DTO
                .collect(Collectors.toList());
    }

    // ─── GET BY ID ─────────────────────────────────────────
    @Override
    public ProductResponseDTO getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found with id: " + id)
                );

        return mapToResponseDTO(product);
    }

    // ─── UPDATE ────────────────────────────────────────────
    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO) {

        // 1. Check product exists
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found with id: " + id)
                );

        // 2. Update fields
        existingProduct.setName(requestDTO.getName());
        existingProduct.setDescription(requestDTO.getDescription());
        existingProduct.setPrice(requestDTO.getPrice());
        existingProduct.setStockQuantity(requestDTO.getStockQuantity());

        // 3. Save updated entity
        Product updatedProduct = productRepository.save(existingProduct);

        return mapToResponseDTO(updatedProduct);
    }

    // ─── DELETE ────────────────────────────────────────────
    @Override
    public void deleteProduct(Long id) {

        // Check exists first before deleting
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }

        productRepository.deleteById(id);
    }

    // ─── PRIVATE HELPER ────────────────────────────────────
    // Reusable method to convert Entity → ResponseDTO
    private ProductResponseDTO mapToResponseDTO(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .build();
    }
}
