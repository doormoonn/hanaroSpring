package com.example.hanaro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hanaro.entity.Member;
import com.example.hanaro.entity.Order;

public interface MemberRepository extends JpaRepository<Member, Integer> {
}
