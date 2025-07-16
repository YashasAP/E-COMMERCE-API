package com.mystore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mystore.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}