package com.example.hanaro.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hanaro.dto.LoginDto;
import com.example.hanaro.security.JwtUtil;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthenticationManager authenticationManager;

	@PostMapping("/signin")
	@Tag(name = "로그인", description = "사용자 로그인")
	public ResponseEntity<?> signin(@RequestBody LoginDto loginRequest) {
		try {
			Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
					loginRequest.getEmail(), loginRequest.getPwd()
				)
			);
			// accessToken, refreshToken, userinfo
			return ResponseEntity.ok(JwtUtil.authenticationToClaims(authenticate));
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid!");
		}

	}
}