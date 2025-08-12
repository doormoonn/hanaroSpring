package com.example.hanaro.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.hanaro.dto.OrderItemsResponseDto;
import com.example.hanaro.dto.OrderResponseDto;
import com.example.hanaro.entity.Cart;
import com.example.hanaro.entity.CartItems;
import com.example.hanaro.entity.Item;
import com.example.hanaro.entity.Member;
import com.example.hanaro.entity.Order;
import com.example.hanaro.entity.OrderItems;
import com.example.hanaro.entity.OrderStatus;
import com.example.hanaro.repository.CartItemsRepository;
import com.example.hanaro.repository.CartRepository;
import com.example.hanaro.repository.ItemRepository;
import com.example.hanaro.repository.MemberRepository;
import com.example.hanaro.repository.OrderItemsRepository;
import com.example.hanaro.repository.OrderRepository;
import com.example.hanaro.service.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final OrderItemsRepository orderItemsRepository;
	private final MemberRepository memberRepository;
	private final CartItemsRepository cartItemsRepository;
	private final CartRepository cartRepository;
	private final ItemRepository itemRepository;
	private final JobLauncher jobLauncher;
	private final Job statJob;


	@Override
	public OrderResponseDto getOrder(int userId) {
		Member member = memberRepository.findById(userId).orElseThrow();
		List<Order> orders = orderRepository.findByMember(member);

		// 가장 최근 주문을 가져오거나, 필요에 따라 모든 주문을 반환할 수 있습니다
		Order order = orders.get(orders.size() - 1); // 최근 주문

		List<OrderItems> orderItems = orderItemsRepository.findByOrder(order);

		List<OrderItemsResponseDto> orderItemsResponseDtos = orderItems.stream().map(o ->
			OrderItemsResponseDto.builder()
				.itemId(o.getItem().getId())
				.itemName(o.getItem().getName())
				.amount(o.getQuantity())
				.price(o.getItem().getPrice())
				.build()
		).toList();

		return OrderResponseDto.builder()
			.orderId(order.getId())
			.status(order.getStatus())
			.createdAt(order.getCreatedAt())
			.orderItems(orderItemsResponseDtos)
			.build();
	}

	@Override
	@Transactional
	public void makeOrders(int userId) {
		Member member = memberRepository.findById(userId)
			.orElseThrow();
		Cart cart = member.getCart();

		// 카트 아이템을 먼저 조회 (삭제하기 전에)
		List<CartItems> cartItemsList = cartItemsRepository.findByCart(cart);

		// 재고 확인
		for (CartItems cartItem : cartItemsList) {
			Item item = cartItem.getItem();
			int requestedQuantity = cartItem.getQuantity();

			if (item.getStock() < requestedQuantity) {
				throw new IllegalArgumentException(
					String.format("상품 '%s'의 재고가 부족합니다. 요청: %d, 재고: %d",
						item.getName(), requestedQuantity, item.getStock())
				);
			}
		}

		Order order = Order.builder()
			.member(member)
			.status(OrderStatus.PAYED)
			.build();

		orderRepository.save(order);

		// OrderItems 생성 및 재고 차감
		for (CartItems cartItem : cartItemsList) {
			Item item = cartItem.getItem();
			int quantity = cartItem.getQuantity();

			// 재고 차감
			item.setStock(item.getStock() - quantity);
			itemRepository.save(item);

			// OrderItems 생성
			OrderItems orderItems = OrderItems.builder()
				.item(item)
				.order(order)
				.quantity(quantity)
				.build();

			orderItemsRepository.save(orderItems);
		}

		// 주문 완료 후 카트 정리
		cartItemsRepository.deleteAllByCart(cart);
		cartRepository.deleteByMember_Id(member.getId());
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public BatchStatus runStatBatch() throws Exception {
		log.info("Service 통계 배치 시작");
		JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
			.addString("saledt", LocalDate.now().toString())
			.toJobParameters();

		return jobLauncher.run(statJob, jobParameters).getStatus();
	}

	@Scheduled(cron = "59 59 23 * * *")
	public void batchStatistics() throws Exception {
		runStatBatch();
	}

	@Scheduled(cron = "0 5 * * * *")
	public void updateStateBatch() throws Exception {
		OrderStatus state = OrderStatus.PAYED;
		while (state != OrderStatus.DELIVERED) {
			LocalDateTime now = LocalDateTime.now();
			System.out.println("now = " + now);

			System.out.println(
				"--> " + state + ", " + state.getNextState() + ": " + now.minusSeconds(state.stateInterval()));
			int affectedRowCount = orderRepository.updateStateBatch(
				state,
				now.minusMinutes(state.stateInterval()),
				state.getNextState());
			System.out.println(" ==> affectedRowCount = " + affectedRowCount);

			state = state.getNextState();
		}
	}

}
