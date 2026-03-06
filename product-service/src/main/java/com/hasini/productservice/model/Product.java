package com.hasini.productservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data                   // Lombok: generates getters, setters, toString, equals
@NoArgsConstructor      // Lombok: generates no-args constructor
@AllArgsConstructor     // Lombok: generates all-args constructor
@Builder                // Lombok: enables builder pattern  Product.builder().name("x").build()
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stockQuantity;
}
