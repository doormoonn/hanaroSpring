package com.example.hanaro.comon.exception;

import static com.example.hanaro.comon.messages.ErrorMessage.*;

import org.springframework.http.HttpStatus;

public class PasswordEmptyException extends CustomException {
	@Override
	public HttpStatus getStatus() {
		return HttpStatus.BAD_REQUEST;
	}

	@Override
	public String getMessage() {
		return PASSWORD_EMPTY_ERROR.getMessage();
	}
}
