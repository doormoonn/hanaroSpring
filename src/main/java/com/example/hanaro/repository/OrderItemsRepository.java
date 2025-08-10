package com.example.hanaro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hanaro.entity.Order;
import com.example.hanaro.entity.OrderItems;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Integer> {
}
