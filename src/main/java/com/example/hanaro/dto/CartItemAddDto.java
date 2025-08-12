package com.example.hanaro.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemAddDto {
	@NotBlank(message = "상품 이름은 필수입니다")
	private String itemName;

	@NotBlank(message = "수량을 선택해주세요")
	@Min(value = 1, message = "1개이상 입력하세요")
	private int quantity;
}
