package com.example.hanaro.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.hanaro.dto.ItemImageDto;
import com.example.hanaro.dto.ItemRequestDto;
import com.example.hanaro.dto.ItemResponseDto;
import com.example.hanaro.entity.Item;
import com.example.hanaro.entity.ItemImages;
import com.example.hanaro.repository.ItemImagesRepository;
import com.example.hanaro.repository.ItemRepository;
import com.example.hanaro.service.ItemService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
	private final ItemRepository repository;
	private final ItemImagesRepository imagesRepository;

	@Override
	public Item saveItem(ItemRequestDto dto) {

		Item item = Item.builder()
			.stock(dto.getStock())
			.content(dto.getContent())
			.name(dto.getName())
			.price(dto.getPrice())
			.build();
		return repository.save(item);
	}

	@Override
	public ItemResponseDto getItem(int id) {
		Item item = repository.findById(id).orElseThrow();
		List<ItemImages> images = imagesRepository.findAllById(Collections.singleton(id));
		List<ItemImageDto> itemImageDto = images
			.stream().map(im -> ItemImageDto.builder()
					.savedir(im.getSavedir())
					.savename(im.getSavename())
					.itemId(im.getId())
					.build()
				).toList();
		return ItemResponseDto.builder()
			.stock(item.getStock())
			.price(item.getPrice())
			.content(item.getContent())
			.name(item.getName())
			.images(itemImageDto)
			.build();
	}
}
