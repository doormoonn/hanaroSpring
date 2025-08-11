package com.example.hanaro.security.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.hanaro.dto.MemberDto;
import com.example.hanaro.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		System.out.println("** SuccessHandler = " + authentication);

		Map<String, Object> claims = JwtUtil.authenticationToClaims(authentication);

		ObjectMapper objectMapper = new ObjectMapper();
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(objectMapper.writeValueAsString(claims));
		out.close();
	}
}
