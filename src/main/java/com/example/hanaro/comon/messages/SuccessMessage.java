package com.example.hanaro.comon.messages;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessMessage {
	REGIST_SUCCESS("회원가입 성공");

	private final String message;

}
