package com.example.hanaro.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hanaro.entity.Cart;
import com.example.hanaro.entity.Member;
import com.example.hanaro.repository.CartRepository;
import com.example.hanaro.repository.MemberRepository;
import com.example.hanaro.service.CartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
	private final CartRepository repository;
	private final MemberRepository memberRepository;

	@Transactional
	@Override
	public void findCartOrMake(String name) {
		Member member = memberRepository.findByNickname(name);
		boolean present = repository.findById(member.getId()).isPresent();
		if (!present) {
			Cart cart = Cart.builder().member(member).build();
			repository.save(cart);
		}
	}

}
