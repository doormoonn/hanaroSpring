package com.example.hanaro.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
import com.example.hanaro.dto.SaleItemStatDto;
import com.example.hanaro.dto.SaleStatResponseDto;
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
import com.example.hanaro.stat.entity.SaleStat;
import com.example.hanaro.stat.repository.SaleStatRepository;

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
	private final SaleStatRepository saleStatRepository;


	@Override
	@Transactional
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
	@Transactional
	public void batchStatistics() throws Exception {
		runStatBatch();
	}

	// 5분마다 실행되도록 크론 표현식을 수정했습니다.
	@Scheduled(cron = "0 */5 * * * *")
	@Transactional
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

	@Override
	public SaleStatResponseDto getSaleStat(String date) {
		SaleStat saleStat = saleStatRepository.findBySaledt(date)
			.orElseThrow(() -> new IllegalArgumentException("No sales data found for the given date: " + date));

		List<SaleItemStatDto> saleItemStatDtos = saleStat.getSaleItemStats().stream()
			.map(itemStat -> SaleItemStatDto.builder()
				.itemName(itemStat.getItem().getName())
				.count(itemStat.getCnt())
				.amount(itemStat.getAmt())
				.build())
			.collect(Collectors.toList());

		return SaleStatResponseDto.builder()
			.saleDate(saleStat.getSaledt())
			.totalOrderCount(saleStat.getOrdercnt())
			.totalAmount(saleStat.getTotamt())
			.saleItems(saleItemStatDtos)
			.build();
	}

	@Override
	public SaleStatResponseDto getTotalSaleStat() {
		List<SaleStat> allSaleStats = saleStatRepository.findAll();

		int totalOrderCount = allSaleStats.stream()
			.mapToInt(SaleStat::getOrdercnt)
			.sum();
		int totalAmount = allSaleStats.stream()
			.mapToInt(SaleStat::getTotamt)
			.sum();

		List<SaleItemStatDto> totalSaleItems = allSaleStats.stream()
			.flatMap(saleStat -> saleStat.getSaleItemStats().stream())
			.collect(Collectors.groupingBy(
				itemStat -> itemStat.getItem().getName(),
				Collectors.collectingAndThen(
					Collectors.toList(),
					list -> {
						int totalCount = list.stream().mapToInt(com.example.hanaro.stat.entity.SaleItemStat::getCnt).sum();
						int totalItemAmount = list.stream().mapToInt(com.example.hanaro.stat.entity.SaleItemStat::getAmt).sum();
						return SaleItemStatDto.builder()
							.itemName(list.get(0).getItem().getName())
							.count(totalCount)
							.amount(totalItemAmount)
							.build();
					}
				)
			))
			.values().stream().collect(Collectors.toList());


		return SaleStatResponseDto.builder()
			.saleDate("Total")
			.totalOrderCount(totalOrderCount)
			.totalAmount(totalAmount)
			.saleItems(totalSaleItems)
			.build();
	}

}