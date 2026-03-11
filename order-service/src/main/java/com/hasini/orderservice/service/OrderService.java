package com.hasini.orderservice.service;

import com.hasini.orderservice.dto.OrderRequestDTO;
import com.hasini.orderservice.dto.OrderResponseDTO;

import java.util.List;

public interface OrderService {

    OrderResponseDTO placeOrder(OrderRequestDTO requestDTO);

    List<OrderResponseDTO> getAllOrders();

    OrderResponseDTO getOrderById(Long id);

    OrderResponseDTO cancelOrder(Long id);
}