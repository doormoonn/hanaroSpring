package com.example.hanaro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.hanaro.entity.ItemImages;

public interface ItemImagesRepository extends JpaRepository<ItemImages, Integer> {
	@Query("select i from ItemImages i where i.item.id = :id")
	List<ItemImages> findAllByItemId(@Param("id") int id);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Transactional
	@Query("delete from ItemImages i where i.item.id = :id")
	void deleteAllByItemId(@Param("id") int id);




}
