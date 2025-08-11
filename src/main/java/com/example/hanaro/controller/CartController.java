package com.example.hanaro.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hanaro.dto.CartItemRequestDto;
import com.example.hanaro.dto.ItemDto;
import com.example.hanaro.service.CartItemsService;
import com.example.hanaro.service.CartService;
import com.example.hanaro.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "장바구니", description = "장바구니 관리 API")
public class CartController {

	private final CartService cartService;
	private final CartItemsService cartItemsService;


	@PostMapping("/items")
	@Operation(summary = "장바구니에 상품 추가", description = "장바구니에 상품을 추가합니다")
	public ResponseEntity<String> addCartItem(@RequestBody CartItemRequestDto cartItemRequestDto, Authentication authentication) {
			String name = authentication.getName();
			log.info("ddddddddddd: {}", name);
		try {
			cartService.findCartOrMake(name);
			cartItemsService.addCartItem(name,cartItemRequestDto);
			log.info("장바구니에 상품 추가 성공: {}", cartItemRequestDto.getItemName());
			return ResponseEntity.ok("장바구니에 상품이 추가되었습니다");
		} catch (Exception e) {
			log.error("장바구니 상품 추가 실패: ", e);
			return ResponseEntity.badRequest().body("장바구니 추가에 실패했습니다");
		}
	}

	@PutMapping("/items/{itemId}")
	@Operation(summary = "장바구니 상품 수량 수정", description = "장바구니에 있는 상품의 수량을 수정합니다")
	public ResponseEntity<String> updateCartItemQuantity(@PathVariable int itemId, @RequestBody int quantity, Authentication authentication) {
		try {
			String name = authentication.getName();
			cartItemsService.updateCartItemQuantity(name ,itemId, quantity);
			return ResponseEntity.ok("상품 수량이 수정되었습니다");
		} catch (Exception e) {
			log.error("장바구니 상품 수정 실패: ", e);
			return ResponseEntity.badRequest().body("상품 수정에 실패했습니다");
		}
	}
//
// 	@DeleteMapping("/items/{itemId}")
// 	@Operation(summary = "장바구니에서 상품 삭제", description = "장바구니에서 특정 상품을 삭제합니다")
// 	public ResponseEntity<String> deleteCartItem(@PathVariable Long itemId) {
// 		try {
// 			cartItemsService.deleteCartItem(itemId);
// 			log.info("장바구니 상품 삭제 성공: itemId={}", itemId);
// 			return ResponseEntity.ok("상품이 삭제되었습니다");
// 		} catch (Exception e) {
// 			log.error("장바구니 상품 삭제 실패: ", e);
// 			return ResponseEntity.badRequest().body("상품 삭제에 실패했습니다");
// 		}
// 	}
//
// 	@GetMapping("/items")
// 	@Operation(summary = "장바구니 조회", description = "장바구니에 있는 모든 상품을 조회합니다")
// 	public ResponseEntity<List<ItemDto>> getCartItems() {
// 		try {
// 			List<ItemDto> cartItems = cartItemsService.getCartItems();
// 			return ResponseEntity.ok(cartItems);
// 		} catch (Exception e) {
// 			log.error("장바구니 조회 실패: ", e);
// 			return ResponseEntity.badRequest().build();
// 		}
// 	}
//
// 	@DeleteMapping
// 	@Operation(summary = "장바구니 전체 비우기", description = "장바구니의 모든 상품을 삭제합니다")
// 	public ResponseEntity<String> clearCart() {
// 		try {
// 			cartService.clearCart();
// 			log.info("장바구니 전체 비우기 성공");
// 			return ResponseEntity.ok("장바구니가 비워졌습니다");
// 		} catch (Exception e) {
// 			log.error("장바구니 비우기 실패: ", e);
// 			return ResponseEntity.badRequest().body("장바구니 비우기에 실패했습니다");
// 		}
// 	}
// }

}
