package com.example.hanaro.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {

	@NotBlank(message = "상품 이름은 필수입니다")
	private String name;

	@Min(value = 1, message = "1개이상 입력하세요")
	private int stock;


	@Min(value = 1, message = "0원이상 입력하세요")
	private int price;

	@NotBlank
	private String content;

	List<MultipartFile> files;
}
