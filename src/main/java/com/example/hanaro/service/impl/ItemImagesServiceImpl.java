package com.example.hanaro.service.impl;

import java.nio.file.Paths;

import org.springframework.stereotype.Service;

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
}
