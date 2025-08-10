package com.example.hanaro.service;

import java.util.Optional;

import com.example.hanaro.dto.ItemRequestDto;
import com.example.hanaro.dto.ItemResponseDto;
import com.example.hanaro.entity.Item;

public interface ItemService {
	Item saveItem(ItemRequestDto dto);

	ItemResponseDto getItem(int id);
}
