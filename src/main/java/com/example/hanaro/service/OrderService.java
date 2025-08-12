package com.example.hanaro.service;

import org.springframework.batch.core.BatchStatus;

import com.example.hanaro.dto.OrderResponseDto;

public interface OrderService {
	void makeOrders(int userId);

	OrderResponseDto getOrder(int userId);

	public BatchStatus runStatBatch() throws Exception;
}
