package com.mystore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mystore.entity.Cart;
import com.mystore.entity.User;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}