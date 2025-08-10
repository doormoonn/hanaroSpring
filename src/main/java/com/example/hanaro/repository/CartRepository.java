package com.example.hanaro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hanaro.entity.Cart;
import com.example.hanaro.entity.Order;

public interface CartRepository extends JpaRepository<Cart,Integer> {
}
