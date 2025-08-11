package com.example.hanaro.comon.messages;

import com.example.hanaro.comon.exception.MemberExistException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

	EXISTING_MEMBER_ERROR("이미 존재하는 회원입니다!"),
	PASSWORD_EMPTY_ERROR("비밀번호가 비어있습니다.");

	private final String message;
}
