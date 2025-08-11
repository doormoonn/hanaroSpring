package com.example.hanaro.service.impl;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hanaro.dto.ItemDto;
import com.example.hanaro.dto.ItemImageDto;
import com.example.hanaro.dto.ItemRequestDto;
import com.example.hanaro.dto.ItemResponseDto;
import com.example.hanaro.dto.ItemUpdateDto;
import com.example.hanaro.entity.Item;
import com.example.hanaro.repository.ItemRepository;
import com.example.hanaro.service.ItemImagesService;
import com.example.hanaro.service.ItemService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
	private final ItemRepository repository;
	private final ItemImagesService imagesService;

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
		List<ItemImageDto> itemImageDto = imagesService.getItemImageDtos(id);
		return ItemResponseDto.builder()
			.stock(item.getStock())
			.price(item.getPrice())
			.content(item.getContent())
			.name(item.getName())
			.images(itemImageDto)
			.build();
	}

	@Override
	public List<ItemDto> getAllItems() {
		List<Item> itemList = repository.findAll();
		List<ItemDto> itemDtos = itemList.stream().map(item ->
			ItemDto.builder()
				.stock(item.getStock())
				.content(item.getContent())
				.price(item.getPrice())
				.createdAt(item.getCreatedAt())
				.updatedAt(item.getUpdatedAt())
				.name(item.getName())
				.build()
		).toList();

		return itemDtos;
	}

	@Override
	@Transactional
	public void updateItem(int id, ItemUpdateDto dto) {
		Item item = Item.builder()
			.stock(dto.getStock())
			.content(dto.getContent())
			.price(dto.getPrice())
			.name(dto.getName())
			.id(id)
			.build();
		repository.save(item);
	}

	@Transactional
	@Override
	public void deleteItem(int id) {
		imagesService.deleteImg(id);
		repository.deleteById(id);
	}

	@Override
	public List<ItemResponseDto> searchItem(String itemname) {
		List<Item> items = repository.findByNameLike(itemname);

		return items.stream().map(item ->
			ItemResponseDto.builder()
				.stock(item.getStock())
				.content(item.getContent())
				.name(item.getName())
				.price(item.getPrice())
				.updatedAt(item.getUpdatedAt())
				.createdAt(item.getCreatedAt())
				.images(imagesService.getItemImageDtos(item.getId()))
				.build()).toList();
	}

}
