package com.example.hanaro.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hanaro.comon.exception.LoginException;
import com.example.hanaro.dto.AdminLoginDto;
import com.example.hanaro.dto.LoginDto;
import com.example.hanaro.entity.Member;
import com.example.hanaro.entity.Role;
import com.example.hanaro.repository.MemberRepository;
import com.example.hanaro.security.JwtUtil;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthenticationManager authenticationManager;
	private final MemberRepository memberService;

	@PostMapping("/signin")
	@Tag(name = "로그인", description = "사용자 로그인")
	@ApiResponses(value = {
	@ApiResponse(responseCode = "400", description = "잘못된 요청",
		content = @Content(mediaType = "application/json",
			examples = @ExampleObject(value = "{\"message\": \"이메일과 비밀번호는 필수입니다.\"}"))),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류",
			content = @Content(mediaType = "application/json",
				examples = @ExampleObject(value = "{\"message\": \"서버에서 오류가 발생했습니다. 잠시 후 다시 시도해주세요.\"}")))
	})
	public ResponseEntity<?> signin(@Validated @RequestBody LoginDto loginRequest) {
		try {
			Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
					loginRequest.getEmail(), loginRequest.getPwd()
				)
			);
			// accessToken, refreshToken, userinfo
			return ResponseEntity.ok(JwtUtil.authenticationToClaims(authenticate));
		} catch (LoginException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(Map.of("message", e.getMessage()));
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(Map.of("message", "이메일 또는 비밀번호가 올바르지 않습니다."));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(Map.of("message", "서버에서 오류가 발생했습니다. 잠시 후 다시 시도해주세요."));
		}

	}


	@PostMapping("/signin/admin")
	@Tag(name = "관리자 로그인", description = "관리자 로그인")
	public ResponseEntity<?> signinAdmin(@RequestBody AdminLoginDto loginRequest) {
		try {
			// 관리자 권한 확인
			Member member = memberService.findByEmail(loginRequest.getEmail()).orElseThrow();
			if (member.getRole() != Role.ROLE_ADMIN) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("관리자 권한이 없습니다.");
			}

			Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
					loginRequest.getEmail(), loginRequest.getPwd()
				)
			);
			return ResponseEntity.ok(JwtUtil.authenticationToClaims(authenticate));
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid!");
		}
	}
}
