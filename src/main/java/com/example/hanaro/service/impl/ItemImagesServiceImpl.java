package com.example.hanaro.service.impl;

import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.hanaro.dto.ItemImageDto;
import com.example.hanaro.entity.Item;
import com.example.hanaro.entity.ItemImages;
import com.example.hanaro.repository.ItemImagesRepository;
import com.example.hanaro.service.ItemImagesService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemImagesServiceImpl implements ItemImagesService {
	private final ItemImagesRepository repository;

	@Override
	public void saveImg(Item item, String imgPath) {
		String fileName = Paths.get(imgPath).getFileName().toString();
		String directory = Paths.get(imgPath).getParent().toString();

		ItemImages itemImage = ItemImages.builder()
			.item(item)
			.savename(fileName)
			.savedir(directory)
			.build();

		repository.save(itemImage);
	}

	@Override
	public List<ItemImageDto> getItemImageDtos(int id) {
		List<ItemImages> images = repository.findAllByitemId(id);
		return images
			.stream().map(im -> ItemImageDto.builder()
				.savedir(im.getSavedir())
				.savename(im.getSavename())
				.itemId(im.getId())
				.build()
			).toList();
	}
}
