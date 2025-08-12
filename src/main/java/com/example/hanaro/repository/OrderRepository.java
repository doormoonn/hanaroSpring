package com.example.hanaro.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.hanaro.entity.Member;
import com.example.hanaro.entity.Order;
import com.example.hanaro.entity.OrderStatus;
import com.example.hanaro.stat.entity.SaleStat;

public interface OrderRepository extends JpaRepository<Order, Integer> {
	List<Order> findByMember(Member member);

	@Modifying(clearAutomatically = true)
	@Query("""
        update Order o
           set o.status = :next,
               o.updatedAt = CURRENT_TIMESTAMP
         where o.status = :current
           and o.updatedAt <= :threshold
    """)
	int updateStateBatch(@Param("current") OrderStatus current,
		@Param("threshold") java.time.LocalDateTime threshold,
		@Param("next") OrderStatus next);

	@Query(value = "select 'today' saledt, count(*) ordercnt, 0 totamt from orders o"
		+ " where o.createdat between concat(:saledt, ' 00:00:00.00') and concat(:saledt, ' 23:59:59.99')", nativeQuery = true)
	public SaleStat getTodayStat(@Param("saledt") String saledt);

	@Query(value =
		"select oi.item_id as item_id, sum(oi.quantity) as quantity, sum(oi.quantity * i.price) as price "
			+ "from orders o "
			+ "inner join OrderItems oi on o.id = oi.order_id " // 테이블명을 `OrderItems`로 수정
			+ "inner join item i on oi.item_id = i.id "
			+ "where o.createdat between concat(:saledt, ' 00:00:00.00') and concat(:saledt, ' 23:59:59.99') "
			+ "group by oi.item_id", nativeQuery = true)
	public List<Object[]> getTodayItemStat(@Param("saledt") String saledt);
}