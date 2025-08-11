package com.example.hanaro.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
	private String name;
	private String content;
	private int stock;
	private int price;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
