package com.hasini.productservice.repository;

import com.hasini.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // JpaRepository gives us these for FREE:
    // save(product)        → INSERT / UPDATE
    // findById(id)         → SELECT WHERE id = ?
    // findAll()            → SELECT * FROM products
    // deleteById(id)       → DELETE WHERE id = ?
    // existsById(id)       → returns boolean
}
