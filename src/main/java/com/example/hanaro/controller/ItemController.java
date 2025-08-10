package com.example.hanaro.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.hanaro.dto.ItemRequestDto;
import com.example.hanaro.dto.ItemResponseDto;
import com.example.hanaro.dto.ItemUpdateDto;
import com.example.hanaro.entity.Item;
import com.example.hanaro.service.ItemImagesService;
import com.example.hanaro.service.ItemService;
import com.example.hanaro.util.FileUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {
	private final FileUtil fileUtil;
	private final ItemService itemService;
	private final ItemImagesService itemImagesService;

	@Tag(name = "file upload")
	@Operation(summary = "Upload POST item")
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> itemUpload(ItemRequestDto itemRequestDto) {
		Item savedItem = itemService.saveItem(itemRequestDto);
		for (MultipartFile multipartFile : itemRequestDto.getFiles()) {
			String uploadImage = fileUtil.uploadImage(multipartFile);
			itemImagesService.saveImg(savedItem, uploadImage);
		}

		if (savedItem != null) {
			return ResponseEntity.ok(itemRequestDto.getName());
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(itemRequestDto.getName() + "is Not Found!");
		}

	@Tag(name = "item")
	@Operation(summary = "Get all items")
	@GetMapping
	public ResponseEntity<List<Item>> getAllItems() {
		List<Item> items = itemService.getAllItems();
		return ResponseEntity.ok(items);
	}


	@Tag(name = "item")
	@Operation(summary = "Get item by ID")
	@GetMapping("/{id}")
	public ResponseEntity<ItemResponseDto> getItem(@PathVariable int id) {
		ItemResponseDto item = itemService.getItem(id);
		if (item != null) {
			return ResponseEntity.ok(item);
		}
		
		return ResponseEntity.notFound().build();
	}

	@Tag(name = "item")
	@Operation(summary = "Update item")
	@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> updateItem(@PathVariable int id, ItemUpdateDto dto) {
		ItemResponseDto item = itemService.getItem(id);
		if (item != null) {
			itemService.updateItem(id, dto);

			return ResponseEntity.ok("Item updated successfully");
		}
			return ResponseEntity.notFound().build();
	}
	//
	// @Tag(name = "item")
	// @Operation(summary = "Delete item")
	// @DeleteMapping("/{id}")
	// public ResponseEntity<String> deleteItem(@PathVariable Long id) {
	// 	Optional<Item> item = itemService.findItemById(id);
	// 	if (!item.isPresent()) {
	// 		return ResponseEntity.notFound().build();
	// 	}
	//
	// 	itemService.deleteItem(id);
	// 	return ResponseEntity.ok("Item deleted successfully");
	// }
}
