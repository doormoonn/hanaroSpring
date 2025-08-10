package com.example.hanaro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hanaro.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {
}
