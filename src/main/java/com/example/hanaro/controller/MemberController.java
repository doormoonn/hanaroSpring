package com.example.hanaro.controller;

import static com.example.hanaro.comon.messages.SuccessMessage.*;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.hanaro.comon.response.HttpResponse;
import com.example.hanaro.dto.ItemDto;
import com.example.hanaro.dto.MemberDto;
import com.example.hanaro.dto.MemberResponseDto;
import com.example.hanaro.dto.RegistMemberDto;
import com.example.hanaro.entity.Member;
import com.example.hanaro.service.impl.MemberServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {

	private final MemberServiceImpl memberService;

	@Tag(name = "member")
	@Operation(summary = "회원가입")
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid RegistMemberDto registMemberDto) {
		memberService.registMember(registMemberDto);

		return ResponseEntity.ok().body(new HttpResponse(HttpStatus.OK, REGIST_SUCCESS.getMessage(), null));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Tag(name = "delteMember")
	@Operation(summary = "회원삭제")
	@DeleteMapping("/admin/{id}")
	public void deleteMember(@PathVariable int id){
		memberService.deleteMember(id);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Tag(name = "memberList")
	@Operation(summary = "멤버 리스트")
	@GetMapping("/admin")
	public ResponseEntity<List<MemberResponseDto>> getAllItems() {
		List<MemberResponseDto> members = memberService.getMemberList();

		return ResponseEntity.ok(members);
	}



}
