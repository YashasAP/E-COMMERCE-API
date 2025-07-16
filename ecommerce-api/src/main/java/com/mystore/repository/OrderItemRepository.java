package com.mystore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mystore.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}