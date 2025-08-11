package com.example.hanaro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hanaro.entity.Member;
import com.example.hanaro.entity.Order;

public interface MemberRepository extends JpaRepository<Member, Integer> {
	Optional<Member> findByEmail(String email);
}
