package com.example.hanaro.config;

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

import com.example.hanaro.entity.Order;
import com.example.hanaro.repository.OrderRepository;
import com.example.hanaro.stat.entity.SaleStat;
import com.example.hanaro.stat.repository.SaleStatRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class OrderBatchConfig {
	private final OrderRepository orderRepository;
	private final SaleStatRepository statRepository;


	@Bean
	public Job statJob(JobRepository jobRepository, Step step) {
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

			List<OrderItem> oitems = orderRepository.getTodayItemStat(saledt);
			List<SaleItemStat> todayItems = oitems.stream()
				.map(oi -> SaleItemStat.builder()
					.saledt(todayStat)
					.item(oi.getItem())
					.amt(oi.getPrice() * oi.getCnt())
					.cnt(oi.getCnt())
					.build())
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
	protected FlatFileItemReader<Memo> memoReader() {
		return new FlatFileItemReaderBuilder<Memo>()
			.name("memoReader")
			.resource(new ClassPathResource("memos.csv"))
			.linesToSkip(1)
			.delimited()
			.names("memoText", "state")
			.fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
				setTargetType(Memo.class);
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