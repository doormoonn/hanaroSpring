package com.example.hanaro.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hanaro.comon.exception.PasswordEmptyException;
import com.example.hanaro.dto.MemberDto;
import com.example.hanaro.dto.MemberResponseDto;
import com.example.hanaro.dto.RegistMemberDto;
import com.example.hanaro.comon.exception.MemberExistException;
import com.example.hanaro.entity.Member;
import com.example.hanaro.entity.Role;
import com.example.hanaro.repository.MemberRepository;
import com.example.hanaro.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {
	private final MemberRepository repository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void registMember(RegistMemberDto registMemberDto) {
		repository.findByEmail(registMemberDto.getEmail()).ifPresent(member -> {
			throw new MemberExistException();
		});
		registMemberDto.setPwd(encodePassword(registMemberDto.getPwd()));

		Member member = Member.builder()
			.email(registMemberDto.getEmail())
			.role(Role.ROLE_USER)
			.nickname(registMemberDto.getNickname())
			.passwd(registMemberDto.getPwd()).build();

		repository.save(member);
	}

	@Override
	public List<MemberResponseDto> getMemberList() {
		List<Member> members = repository.findAll();
		return members.stream().map(m ->
			MemberResponseDto.builder()
				.email(m.getEmail())
				.nickname(m.getNickname())
				.id(m.getId())
				.build()
		).toList();

	}

	@Override
	public void deleteMember(int id) {
		repository.deleteById(id);
	}

	private String encodePassword(String originPwd) {
		if (originPwd == null || originPwd.isEmpty()) {
			throw new PasswordEmptyException();
		}

		return passwordEncoder.encode(originPwd);
	}
}
