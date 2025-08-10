package com.example.hanaro.repository;

import static com.example.hanaro.entity.Role.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.hanaro.RepositoryTest;
import com.example.hanaro.entity.Member;

class MemberRepositoryTest extends RepositoryTest {

	@Autowired
	private MemberRepository memberRepository;

	private Member testMember;

	@BeforeEach
	void setUp() {
		Member member = Member.builder()
			.email("test@naver.com")
			.passwd("asdf")
			.role(ROLE_USER)
			.nickname("yoyo")
			.build();
		testMember = memberRepository.save(member);
	}

	@Test
	@Order(1)
	void createTest() {
		Member member = Member.builder()
			.email("create@gmail.com")
			.passwd("asdf")
			.nickname("mo")
			.role(ROLE_USER)
			.build();

		Member savedMember = memberRepository.save(member);

		assertThat(savedMember.getId()).isNotNull();
		assertThat(savedMember.getEmail()).isEqualTo("create@gmail.com");
		assertThat(savedMember.getNickname()).isEqualTo("mo");
		assertThat(savedMember.getRole()).isEqualTo(ROLE_USER);
	}

	@Test
	@Order(2)
	void readTest() {
		Member foundMember = memberRepository.findById(1).orElse(null);

		assertThat(foundMember).isNotNull();
		assertThat(foundMember.getId()).isEqualTo(testMember.getId());
	}

	@Test
	@Order(3)
	void updateTest() {
		Member member = memberRepository.findById(testMember.getId()).orElseThrow();
		Member updated = Member.builder()
			.id(member.getId())
			.email(member.getEmail())
			.passwd(member.getPasswd())
			.nickname("updateyoyo")
			.auth(member.getAuth())
			.build();
		memberRepository.save(updated);

		Member foundMember = memberRepository.findById(testMember.getId()).orElseThrow();
		assertThat(foundMember.getNickname()).isEqualTo("updateyoyo");
	}

	@Test
	@Order(4)
	void deleteTest() {
		memberRepository.deleteById(testMember.getId());

		assertThat(memberRepository.findById(testMember.getId())).isEmpty();
	}





}