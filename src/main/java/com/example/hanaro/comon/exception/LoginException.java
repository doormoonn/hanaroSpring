package com.example.hanaro.comon.exception;

import org.springframework.http.HttpStatus;

import com.example.hanaro.comon.messages.ErrorMessage;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LoginException extends CustomException {
	@Override
	public HttpStatus getStatus() {
		return HttpStatus.CONFLICT;
	}

	@Override
	public String getMessage() {
		return ErrorMessage.LOGIN_TYPE_VALIDATE_ERROR.getMessage();
	}
}
