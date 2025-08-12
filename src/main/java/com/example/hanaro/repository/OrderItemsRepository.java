package com.example.hanaro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hanaro.entity.Order;
import com.example.hanaro.entity.OrderItems;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Integer> {

	List<OrderItems> findAllByOrder_Id(int orderId);

	List<OrderItems> findByOrder(Order order);
}
