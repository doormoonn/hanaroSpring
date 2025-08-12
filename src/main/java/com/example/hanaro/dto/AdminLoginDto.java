package com.example.hanaro.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginDto {
	@NotBlank(message = "이메일은 필수입력 값입니다.")
	private String email;

	@NotBlank(message = "비밀번호는 필수입력 값입니다.")
	private String pwd;
}
