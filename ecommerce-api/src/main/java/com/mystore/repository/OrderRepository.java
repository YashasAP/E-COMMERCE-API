package com.mystore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mystore.entity.Order;
import com.mystore.entity.User;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
