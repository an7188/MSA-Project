package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
public interface OrderService {
    // 주문 저장
    OrderDto createOrder(OrderDto orderDetails);
    // 주문 아이디로 검색
    OrderDto getOrderByOrderId(String orderId);
    // 전채 주문 내역
    Iterable<OrderEntity> getOrdersByUserId(String userId);
}
