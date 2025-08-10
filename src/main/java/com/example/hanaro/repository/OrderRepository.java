package com.example.hanaro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hanaro.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
