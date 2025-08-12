package com.example.hanaro.stat.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleStat {
	@Id
	@Column(length = 10)
	private String saledt;

	private int ordercnt;

	private int totamt;

	// 이 부분을 추가하거나 수정해야 합니다.
	@OneToMany(mappedBy = "saledt", cascade = CascadeType.ALL)
	private List<SaleItemStat> saleItemStats;

	@Override
	public String toString() {
		return "SaleStat{" +
			"saledt='" + saledt + '\'' +
			", ordercnt=" + ordercnt +
			", totamt=" + totamt +
			", saleItemStats=" + (saleItemStats == null ? 0 : saleItemStats.size()) +
			'}';
	}
}