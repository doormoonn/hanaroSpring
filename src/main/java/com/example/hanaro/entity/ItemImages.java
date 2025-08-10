package com.example.hanaro.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemImages {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String savename;

	@Column(nullable = false)
	private String savedir;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", foreignKey = @ForeignKey(name = "fk_ItemImages_item_id"))
	private Item item;
}
