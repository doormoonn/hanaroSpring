package com.example.hanaro.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hanaro.dto.CartItemRequestDto;
import com.example.hanaro.dto.CartItemResponseDto;
import com.example.hanaro.dto.CartItemUpdateDto;
import com.example.hanaro.entity.Cart;
import com.example.hanaro.entity.CartItems;
import com.example.hanaro.entity.Item;
import com.example.hanaro.entity.Member;
import com.example.hanaro.repository.CartItemsRepository;
import com.example.hanaro.repository.CartRepository;
import com.example.hanaro.repository.ItemRepository;
import com.example.hanaro.repository.MemberRepository;
import com.example.hanaro.service.CartItemsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemsServiceImpl implements CartItemsService {

	private final CartItemsRepository repository;
	private final ItemRepository itemRepository;
	private final CartRepository cartRepository;
	private final MemberRepository memberRepository;

	@Transactional
	@Override
	public void addCartItem(String name,CartItemRequestDto cartItemRequestDto) {
		Item item = itemRepository.findFirstByName(cartItemRequestDto.getItemName()).orElseThrow();
		Member member = memberRepository.findByNickname(name);

		Cart cart = cartRepository.findById(member.getId()).orElseThrow();
		CartItems items = CartItems.builder()
			.cart(cart)
			.item(item)
			.quantity(cartItemRequestDto.getQuantity())
			.build();

		repository.save(items);
	}

	@Transactional
	@Override
	public void updateCartItemQuantity(String name, int itemId, int quantity) {
		Member member = memberRepository.findByNickname(name);
		Cart cart = cartRepository.findById(member.getId()).orElseThrow();
		Item item = itemRepository.findById(itemId).orElseThrow();
		CartItems cartItems = repository.findByCartAndItem(cart,item);
		cartItems.setQuantity(quantity);

		repository.save(cartItems);
	}

}
