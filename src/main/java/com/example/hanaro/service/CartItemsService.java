package com.example.hanaro.service;

import java.util.List;

import com.example.hanaro.dto.CartItemAddDto;
import com.example.hanaro.dto.CartItemDeleteDto;
import com.example.hanaro.dto.CartItemRequestDto;
import com.example.hanaro.dto.CartItemResponseDto;

public interface CartItemsService {
	void addCartItem(String name, CartItemAddDto cartItemAddDto);

	void updateCartItemQuantity(String name,int itemId, int quantity);

	void deleteCartItem(String name, CartItemDeleteDto cartItemDeleteDto);

	List<CartItemResponseDto> getCartItems(String name);
}
