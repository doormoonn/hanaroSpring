package com.example.hanaro.service;

import java.util.List;

import com.example.hanaro.dto.ItemDto;
import com.example.hanaro.dto.ItemRequestDto;
import com.example.hanaro.dto.ItemResponseDto;
import com.example.hanaro.dto.ItemUpdateDto;
import com.example.hanaro.entity.Item;

public interface ItemService {
	Item saveItem(ItemRequestDto dto);

	ItemResponseDto getItem(int id);

	List<ItemDto> getAllItems();

	void updateItem(int id, ItemUpdateDto dto);

	void deleteItem(int id);

	List<ItemResponseDto> searchItem(String itemname);
}
