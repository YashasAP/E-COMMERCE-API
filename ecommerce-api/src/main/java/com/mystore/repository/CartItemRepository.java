package com.mystore.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mystore.entity.Cart;
import com.mystore.entity.CartItem;
import com.mystore.entity.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}
