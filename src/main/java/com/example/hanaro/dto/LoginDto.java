package com.example.hanaro.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
	@Email(message = "올바른 이메일 형식이어야 합니다.")
	@NotBlank(message = "이메일은 필수입력 값입니다.")
	private String email;

	@NotBlank(message = "비밀번호는 필수입력 값입니다.")
	@Pattern(
		regexp = "^(?=.*[가-힣a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,20}$",
		message = "비밀번호는 6~20자 사이이며, 문자, 숫자, 특수문자를 각각 최소 1개 포함해야 합니다."
	)
	private String pwd;

}