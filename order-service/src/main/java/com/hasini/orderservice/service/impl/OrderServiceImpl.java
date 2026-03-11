package com.hasini.orderservice.service.impl;

import com.hasini.orderservice.client.ProductClient;
import com.hasini.orderservice.dto.OrderRequestDTO;
import com.hasini.orderservice.dto.OrderResponseDTO;
import com.hasini.orderservice.model.Order;
import com.hasini.orderservice.repository.OrderRepository;
import com.hasini.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    @Override
    public OrderResponseDTO placeOrder(OrderRequestDTO requestDTO) {

        ProductClient.ProductResponse product =
                productClient.getProductById(requestDTO.getProductId());

        Double totalPrice = product.getPrice() * requestDTO.getQuantity();

        Order order = Order.builder()
                .productId(requestDTO.getProductId())
                .quantity(requestDTO.getQuantity())
                .totalPrice(totalPrice)
                .status("PLACED")
                .createdAt(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);

        return mapToResponseDTO(savedOrder, product.getName());
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(order -> {
                    ProductClient.ProductResponse product =
                            productClient.getProductById(order.getProductId());
                    return mapToResponseDTO(order, product.getName());
                })
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found with id: " + id));

        ProductClient.ProductResponse product =
                productClient.getProductById(order.getProductId());

        return mapToResponseDTO(order, product.getName());
    }

    @Override
    public OrderResponseDTO cancelOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found with id: " + id));

        order.setStatus("CANCELLED");
        Order updatedOrder = orderRepository.save(order);

        ProductClient.ProductResponse product =
                productClient.getProductById(order.getProductId());

        return mapToResponseDTO(updatedOrder, product.getName());
    }

    private OrderResponseDTO mapToResponseDTO(Order order, String productName) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .productId(order.getProductId())
                .productName(productName)
                .quantity(order.getQuantity())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }
}