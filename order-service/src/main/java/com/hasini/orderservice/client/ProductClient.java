package com.hasini.orderservice.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductResponse getProductById(@PathVariable("id") Long id);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ProductResponse {
        private Long id;
        private String name;
        private String description;
        private Double price;
        private Integer stockQuantity;
    }
}