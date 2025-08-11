package com.example.hanaro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hanaro.entity.Item;


public interface ItemRepository extends JpaRepository<Item, Integer> {

	List<Item> findByNameLike(String itemname);

	Optional<Item> findFirstByName(String name);
}
