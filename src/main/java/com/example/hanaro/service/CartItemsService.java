package com.example.hanaro.service;

import com.example.hanaro.dto.CartItemRequestDto;

public interface CartItemsService {
	void addCartItem(String name, CartItemRequestDto cartItemRequestDto);

	void updateCartItemQuantity(String name,int itemId, int quantity);
}
