package com.example.hanaro.service;

import org.springframework.batch.core.BatchStatus;

import com.example.hanaro.dto.OrderResponseDto;
import com.example.hanaro.dto.SaleStatResponseDto;

public interface OrderService {
	void makeOrders(int userId);

	OrderResponseDto getOrder(int userId);

	public BatchStatus runStatBatch() throws Exception;

	SaleStatResponseDto getSaleStat(String date);

	SaleStatResponseDto getTotalSaleStat();
}