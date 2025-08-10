package com.example.hanaro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hanaro.entity.ItemImages;
import com.example.hanaro.entity.Order;

public interface ItemImagesRepository extends JpaRepository<ItemImages, Integer> {
}
