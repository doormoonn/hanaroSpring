package com.example.hanaro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hanaro.entity.CartItems;

public interface CartItemsRepository extends JpaRepository<CartItems, Integer> {
}
