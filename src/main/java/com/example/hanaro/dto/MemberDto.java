package com.example.hanaro.dto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.example.hanaro.entity.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto extends User {
	private int id;

	private String nickname;

	private String email;

	private Role role;

	private String passwd;


	public MemberDto(String email, String passwd, String nickname, Role role) {
		super(nickname, passwd, Collections.singletonList(new SimpleGrantedAuthority(role.name())));
		this.email = email;
		this.passwd = passwd;
		this.nickname = nickname;
		this.role = role;
	}

	public Map<String, Object> getClaims() {
		Map<String, Object> map = new HashMap<>();
		map.put("email", email);
		map.put("nickname", nickname);
		map.put("pwd", passwd);
		map.put("role", role);

		return map;
	}
}
