package com.example.hanaro.repository;

import com.example.hanaro.RepositoryTest;
import com.example.hanaro.entity.CartItems;
import com.example.hanaro.entity.Item;
import com.example.hanaro.entity.Order;
import com.example.hanaro.entity.OrderItems;
import com.example.hanaro.entity.Role;
import com.example.hanaro.stat.repository.SaleItemStatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemRepositoryTest extends RepositoryTest {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private OrderItemsRepository orderItemsRepository;

	@Autowired
	private CartItemsRepository cartItemsRepository;

	@Autowired
	private SaleItemStatRepository saleItemStatRepository;

	@BeforeEach
	void setUp() {
		// 외래 키를 가진 자식 테이블의 데이터를 먼저 삭제합니다.
		saleItemStatRepository.deleteAll();
		orderItemsRepository.deleteAll();
		cartItemsRepository.deleteAll();

		// 부모 테이블의 데이터를 삭제합니다.
		itemRepository.deleteAll();
	}

	@Test
	@org.junit.jupiter.api.Order(1)
	void findFirstByNameTest() {
		// given a persisted item
		Item item = Item.builder()
			.name("Red Apple")
			.price(3000)
			.stock(100)
			.content("A sweet red apple")
			.build();
		itemRepository.save(item);

		// when searching by exact name
		Optional<Item> found = itemRepository.findFirstByName("Red Apple");

		// then the item should be found
		assertThat(found).isPresent();
		assertThat(found.get().getPrice()).isEqualTo(3000);
		assertThat(found.get().getStock()).isEqualTo(100);
	}

	@Test
	@org.junit.jupiter.api.Order(2)
	void findByNameLikeTest() {
		// given multiple items
		Item i1 = Item.builder().name("Green Tea").price(1500).stock(50).content("Green").build();
		Item i2 = Item.builder().name("Black Tea").price(1600).stock(60).content("Black").build();
		Item i3 = Item.builder().name("Herbal Tea").price(2000).stock(70).content("Herbal").build();
		itemRepository.saveAll(List.of(i1, i2, i3));

		// when searching for items containing 'Tea'
		List<Item> results = itemRepository.findByNameLike("%Tea%");

		// then all tea items should be returned
		assertThat(results).hasSize(3);
		assertThat(results).extracting(Item::getName)
			.containsExactlyInAnyOrder("Green Tea", "Black Tea", "Herbal Tea");
	}
}
