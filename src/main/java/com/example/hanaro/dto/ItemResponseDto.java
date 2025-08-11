package com.example.hanaro.dto;

import java.time.LocalDateTime;
import java.util.List;

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
public class ItemResponseDto {
	private int id;
	private String name;
	private int stock;
	private int price;
	private List<ItemImageDto> images;
	private String content;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
