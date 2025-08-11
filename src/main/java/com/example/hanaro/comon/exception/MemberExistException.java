package com.example.hanaro.comon.exception;

import org.springframework.http.HttpStatus;

import com.example.hanaro.comon.messages.ErrorMessage;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MemberExistException extends CustomException {
	@Override
	public HttpStatus getStatus() {
		return HttpStatus.CONFLICT;
	}

	@Override
	public String getMessage() {
		return ErrorMessage.EXISTING_MEMBER_ERROR.getMessage();
	}
}
