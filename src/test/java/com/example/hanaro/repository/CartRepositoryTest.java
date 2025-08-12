package com.example.hanaro.repository;

import com.example.hanaro.RepositoryTest;
import com.example.hanaro.entity.Cart;
import com.example.hanaro.entity.CartItems;
import com.example.hanaro.entity.Item;
import com.example.hanaro.entity.Member;
import com.example.hanaro.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CartRepositoryTest extends RepositoryTest {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemsRepository cartItemsRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private ItemRepository itemRepository;

	private Member testMember;
	private Item testItem;

	@BeforeEach
	void setUp() {
		// 테스트 격리를 위해 리포지토리 정리
		cartItemsRepository.deleteAll();
		cartRepository.deleteAll();
		memberRepository.deleteAll();
		itemRepository.deleteAll();

		// 테스트를 위한 회원 생성
		testMember = Member.builder()
			.email("cart_test@example.com")
			.passwd("password123")
			.nickname("cartTester")
			.role(Role.ROLE_USER)
			.build();
		memberRepository.save(testMember);

		// 테스트를 위한 상품 생성
		testItem = Item.builder()
			.name("Test Item")
			.price(1000)
			.stock(50)
			.content("Test content for item")
			.build();
		itemRepository.save(testItem);
	}

	@Test
	@Order(1)
	void addCartAndCartItem() {
		// Cart 생성 및 저장
		Cart cart = Cart.builder()
			.member(testMember)
			.build();
		cartRepository.save(cart);

		// CartItem 생성 및 저장
		CartItems cartItem = CartItems.builder()
			.cart(cart)
			.item(testItem)
			.quantity(2)
			.build();
		cartItemsRepository.save(cartItem);

		// 저장된 Cart와 CartItem 조회
		Cart foundCart = cartRepository.findById(cart.getId()).orElseThrow();
		CartItems foundItem = cartItemsRepository.findById(cartItem.getId()).orElseThrow();

		// 어설션: 값이 올바르게 저장되었는지 확인
		assertThat(foundCart.getMember().getId()).isEqualTo(testMember.getId());
		assertThat(foundItem.getCart().getId()).isEqualTo(cart.getId());
		assertThat(foundItem.getItem().getId()).isEqualTo(testItem.getId());
		assertThat(foundItem.getQuantity()).isEqualTo(2);

		// findByMember 메서드 테스트
		Optional<Cart> optionalCart = cartRepository.findByMember(testMember);
		assertThat(optionalCart).isPresent();
		assertThat(optionalCart.get().getId()).isEqualTo(cart.getId());
	}

	@Test
	@Order(2)
	void findCartItemByCartAndItem() {
		// Cart 생성 및 저장
		Cart cart = Cart.builder().member(testMember).build();
		cartRepository.save(cart);

		// CartItem 생성 및 저장
		CartItems cartItem = CartItems.builder()
			.cart(cart)
			.item(testItem)
			.quantity(3)
			.build();
		cartItemsRepository.save(cartItem);

		// findByCartAndItem 메서드 테스트
		CartItems found = cartItemsRepository.findByCartAndItem(cart, testItem);

		// 어설션: 찾은 CartItem이 올바른지 확인
		assertThat(found).isNotNull();
		assertThat(found.getQuantity()).isEqualTo(3);
		assertThat(found.getItem().getName()).isEqualTo("Test Item");
	}

	@Test
	@Order(3)
	void updateCartItemQuantity() {
		// Cart 생성 및 저장
		Cart cart = Cart.builder().member(testMember).build();
		cartRepository.save(cart);
		// CartItem 생성 및 저장
		CartItems cartItem = CartItems.builder()
			.cart(cart)
			.item(testItem)
			.quantity(1)
			.build();
		cartItemsRepository.save(cartItem);

		// 수량 업데이트
		CartItems saved = cartItemsRepository.findById(cartItem.getId()).orElseThrow();
		saved.setQuantity(5);
		cartItemsRepository.save(saved);

		// 업데이트된 CartItem 조회 및 확인
		CartItems updated = cartItemsRepository.findById(cartItem.getId()).orElseThrow();
		assertThat(updated.getQuantity()).isEqualTo(5);
	}

	@Test
	@Order(4)
	void deleteCartItem() {
		// Cart 생성 및 저장
		Cart cart = Cart.builder().member(testMember).build();
		cartRepository.save(cart);

		// CartItem 생성 및 저장
		CartItems cartItem = CartItems.builder()
			.cart(cart)
			.item(testItem)
			.quantity(1)
			.build();
		cartItemsRepository.save(cartItem);

		int id = cartItem.getId();

		// CartItem 삭제
		cartItemsRepository.deleteById(id);

		// 삭제 후 조회하여 비어있는지 확인
		assertThat(cartItemsRepository.findById(id)).isEmpty();
	}
}
