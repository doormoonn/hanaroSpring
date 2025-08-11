package com.example.hanaro.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.hanaro.dto.MemberDto;
import com.example.hanaro.entity.Member;
import com.example.hanaro.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberDetailServiceImpl implements UserDetailsService {
	private final MemberRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("*** Service.loadUserByUsername: " + username);
		Member member = repository.getByEmail(username);

		if (member == null)
			throw new UsernameNotFoundException(username + " is Not Found!!");

		MemberDto dto = new MemberDto(member.getEmail(), member.getPasswd(), member.getNickname(),
			member.getRole());
		System.out.println("dto = " + dto);
		return dto;
	}
}