package com.example.hanaro.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.hanaro.dto.MemberDto;
import com.example.hanaro.entity.Role;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final AntPathMatcher pathMatcher = new AntPathMatcher();

	private final String[] excludePatterns = {
		"/api/auth/signin",
		"/api/auth/signin/admin",
		"/member/signup",
		"/api/public/**",
		"/actuator/**",
		"/swagger-ui/**",
		"/v3/api-docs/**",
	};


	@Override
	protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
		String path = request.getRequestURI();
		System.out.println("** path = " + path);
		boolean isNotNeed = Arrays.stream(excludePatterns)
			.anyMatch(pattern -> pathMatcher.match(pattern, path));
		System.out.println("isNotNeed = " + isNotNeed);
		return isNotNeed;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain) throws ServletException, IOException {
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		try {
			System.out.println("** JwtAuthenticationFilter.doFilterInternal:" + authHeader.substring(7));
			Map<String, Object> claims = JwtUtil.validateToken(authHeader.substring(7));

			String email = (String)claims.get("email");
			String nickname = (String)claims.get("nickname");
			Role role = Role.valueOf((String)claims.get("role"));
			MemberDto dto = new MemberDto(email, "", nickname, role);
			UsernamePasswordAuthenticationToken authenticationToken = new
				UsernamePasswordAuthenticationToken(dto, null, dto.getAuthorities());

			// 올바른 Authorization을 저장하여 어디서든 불러올 수 있다!
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			response.setContentType("application/json");
			ObjectMapper objectMapper = new ObjectMapper();
			PrintWriter out = response.getWriter();
			out.println(objectMapper.writeValueAsString(Map.of("error", "ERROR_ACCESS_TOKEN")));
			out.close();
		}
	}
}