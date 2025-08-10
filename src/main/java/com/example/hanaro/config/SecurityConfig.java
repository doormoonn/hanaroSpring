package com.example.hanaro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	// private final JwtUtil jwtUtil;
	//
	// public SecurityConfig(JwtUtil jwtUtil) {
	// 	this.jwtUtil = jwtUtil;
	// }

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		// csrf disable: jwt가 stateless 이기 때문에 공격을 방어하지 않아도 됨
		http
			.csrf((auth) -> auth.disable());
		//From 로그인 방식 disable
		http
			.formLogin((auth) -> auth.disable());
		//http basic 인증 방식 disable
		http
			.httpBasic((auth) -> auth.disable());

		// 경로별 인가 작업
		http
			.authorizeHttpRequests((auth) -> auth
				.requestMatchers("/**").permitAll()
				.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
				.anyRequest().authenticated());

		//JWT 필터 등록
		// http
		// 	.addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

		// 세션 설정
		http.sessionManagement(
			(session)-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		);

		return http.build();
	}
}
