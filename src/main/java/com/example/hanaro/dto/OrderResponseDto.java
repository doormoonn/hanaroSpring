package com.example.hanaro.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.hanaro.entity.OrderItems;
import com.example.hanaro.entity.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
	private int orderId;
	private LocalDateTime createdAt;
	private OrderStatus status;

	private List<OrderItemsResponseDto> orderItems;

}
