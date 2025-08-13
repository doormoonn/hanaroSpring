package com.example.hanaro.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.hanaro.dto.OrderResponseDto;
import com.example.hanaro.dto.SaleStatResponseDto;
import com.example.hanaro.service.OrderService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "주문", description = "주문 관리 API")
public class OrderController {
	private final OrderService orderService;

	@PostMapping("/users/{userId}/orders")
	public ResponseEntity<?> makeOrder(@PathVariable("userId") int userId) {
		orderService.makeOrders(userId);
		return ResponseEntity.ok().build();
	}


	@GetMapping("/users/{userId}/orders")
	public ResponseEntity<OrderResponseDto> getUserOrders(@PathVariable("userId") int userId) {
		OrderResponseDto order = orderService.getOrder(userId);
		return ResponseEntity.ok(order);
	}

	@GetMapping("/sales")
	public ResponseEntity<SaleStatResponseDto> getSales(@RequestParam String date) {
		SaleStatResponseDto saleStat = orderService.getSaleStat(date);
		return ResponseEntity.ok(saleStat);
	}

	@GetMapping("/sales/total")
	public ResponseEntity<SaleStatResponseDto> getTotalSales() {
		SaleStatResponseDto saleStat = orderService.getTotalSaleStat();
		return ResponseEntity.ok(saleStat);
	}


}