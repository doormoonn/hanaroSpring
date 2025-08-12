package com.example.hanaro.stat.entity;


import com.example.hanaro.entity.Item;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class SaleItemStat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "saledt")
	private SaleStat saledt;

	@ManyToOne
	@JoinColumn(name = "item")
	private Item item;

	private int cnt;
	private int amt;

	@Override
	public String toString() {
		return "SaleItemStat{" +
			"id=" + id +
			", saledt=" + saledt.getSaledt() +
			", item=" + item.getName() +
			", cnt=" + cnt +
			", amt=" + amt +
			'}';
	}
}