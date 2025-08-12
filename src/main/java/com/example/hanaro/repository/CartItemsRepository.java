package com.example.hanaro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.example.hanaro.entity.Cart;
import com.example.hanaro.entity.CartItems;
import com.example.hanaro.entity.Item;

public interface CartItemsRepository extends JpaRepository<CartItems, Integer> {


	CartItems findByCartAndItem(Cart cart, Item item);

	List<CartItems> findCartItemsByCart(Cart cart);

	List<CartItems> findByCart(Cart cart);

	void deleteAllByCart(Cart cart);
}
