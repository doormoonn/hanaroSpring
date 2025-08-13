package com.example.hanaro.stat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hanaro.stat.entity.SaleStat;

public interface SaleStatRepository extends JpaRepository<SaleStat, String> {

	Optional<SaleStat> findBySaledt(String saledt);
}