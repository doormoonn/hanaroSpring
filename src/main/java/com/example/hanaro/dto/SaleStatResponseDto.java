package com.example.hanaro.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleStatResponseDto {
	private String saleDate;
	private int totalOrderCount;
	private int totalAmount;
	private List<SaleItemStatDto> saleItems;
}