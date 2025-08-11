package com.example.hanaro.service;

import java.util.List;

import com.example.hanaro.dto.MemberResponseDto;
import com.example.hanaro.dto.RegistMemberDto;

public interface MemberService {
	void registMember(RegistMemberDto registMemberDto);

	List<MemberResponseDto> getMemberList();

	void deleteMember(int id);
}
