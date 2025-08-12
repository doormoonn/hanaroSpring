package com.example.hanaro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemsResponseDto {
	private int itemId;
	private String itemName;
	private int amount;
	private int price;
}
