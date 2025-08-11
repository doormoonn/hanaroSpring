package com.example.hanaro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hanaro.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {
	Optional<Member> findByEmail(String email);

	Member getByEmail(String email);

	Member findByNickname(String nickname);
}
