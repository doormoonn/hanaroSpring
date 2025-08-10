package com.example.hanaro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hanaro.entity.ItemImages;
import com.example.hanaro.entity.Order;

public interface ItemImagesRepository extends JpaRepository<ItemImages, Integer> {
	@Query("select i from ItemImages i where i.item.id = :id")
	List<ItemImages> findAllByitemId(@Param("id") int id);
}
