package com.example.hanaro.security.exception;

public class CustomJwtException extends RuntimeException {
	public CustomJwtException(String msg) {
		super("JwtErr:" + msg);
	}
}