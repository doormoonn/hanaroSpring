package com.example.hanaro.config;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import org.springframework.transaction.PlatformTransactionManager;

import com.example.hanaro.entity.Item;
import com.example.hanaro.entity.Order;
import com.example.hanaro.entity.OrderItems;
import com.example.hanaro.repository.ItemRepository;
import com.example.hanaro.repository.OrderRepository;
import com.example.hanaro.stat.entity.SaleItemStat;
import com.example.hanaro.stat.entity.SaleStat;
import com.example.hanaro.stat.repository.SaleStatRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class OrderBatchConfig {
	private final OrderRepository orderRepository;
	private final SaleStatRepository statRepository;
	private final ItemRepository itemRepository;


	@Bean
	public Job statJob(JobRepository jobRepository, Step statStep) {
		return new JobBuilder("statJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(statStep)
			.build();
	}

	@Bean
	public Step statStep(JobRepository jobRepository,
		PlatformTransactionManager transactionManager) {
		return new StepBuilder("csvStep", jobRepository)
			.<SaleStat, SaleStat>chunk(5, transactionManager)
			.reader(statReader())
			.processor(statProcessor(null))
			.writer(statWriter())
			.build();
	}

	@Bean
	@StepScope
	public ItemProcessor<SaleStat, SaleStat> statProcessor(@Value("#{jobParameters['saledt']}") String saledt) {
		System.out.println("xxx - saledt = " + saledt);
		return stat -> {
			SaleStat todayStat = SaleStat.builder()
				.saledt(saledt)
				.ordercnt(stat.getOrdercnt())
				.build();
			System.out.println("bbb - todayStat = " + todayStat);

			// 네이티브 쿼리 반환 타입인 List<Object[]>를 사용
			List<Object[]> oitemsData = orderRepository.getTodayItemStat(saledt);
			List<SaleItemStat> todayItems = oitemsData.stream()
				.map(itemData -> {
					// Object[] 배열에서 값 추출 및 타입 캐스팅
					Integer itemId = (Integer) itemData[0];
					Long quantity = (Long) itemData[1];
					Long totalPrice = (Long) itemData[2];

					// Item 객체를 조회하여 SaleItemStat에 설정
					Item item = itemRepository.findById(itemId)
						.orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + itemId));

					return SaleItemStat.builder()
						.saledt(todayStat)
						.item(item)
						.amt(totalPrice.intValue()) // 총액
						.cnt(quantity.intValue()) // 수량
						.build();
				})
				.toList();
			System.out.println("bbb - todayItems = " + todayItems);

			todayStat.setSaleItemStats(todayItems);
			int sum = todayItems.stream().mapToInt(SaleItemStat::getAmt).sum();
			todayStat.setTotamt(sum);

			return todayStat;
		};
	}

	@Bean
	@StepScope
	// memoReader(@Value("#{jobParameters['filePath']}") String filePath) {
	protected FlatFileItemReader<SaleStat> statReader() {
		return new FlatFileItemReaderBuilder<SaleStat>()
			.name("statReader")
			.resource(new ClassPathResource("stat.csv"))
			.linesToSkip(1)
			.delimited()
			.names("saleText", "state")
			.fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
				setTargetType(SaleStat.class);
			}}).build();
	}

	// @Bean
	// @StepScope
	// public RepositoryItemReader repReader() {
	// 	return new RepositoryItemReaderBuilder<Member>(MemberRepository )
	// 		.methodName("findAll")
	// 		.build();
	// }

	@Bean
	public RepositoryItemWriter<SaleStat> statWriter() {
		return new RepositoryItemWriterBuilder<SaleStat>()
			.repository(statRepository)
			.methodName("save")
			.build();
	}

}