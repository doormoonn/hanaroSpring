package com.example.hanaro.service;

import java.util.List;

import com.example.hanaro.dto.ItemImageDto;
import com.example.hanaro.entity.Item;

public interface ItemImagesService {
	void saveImg(Item item, String imgPath);
	List<ItemImageDto> getItemImageDtos(int id);
	void deleteImg(int id);
}
