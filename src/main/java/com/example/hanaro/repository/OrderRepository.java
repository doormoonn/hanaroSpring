package com.example.hanaro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hanaro.entity.Member;
import com.example.hanaro.entity.Order;
import com.example.hanaro.entity.OrderItems;
import com.example.hanaro.entity.OrderStatus;
import com.example.hanaro.stat.entity.SaleStat;

public interface OrderRepository extends JpaRepository<Order, Integer> {
	List<Order> findByMember(Member member);
	@Modifying(clearAutomatically = true)
	@Query("""
        update Order o
           set o.status = :next,
               o.statedAt = CURRENT_TIMESTAMP
         where o.status = :current
           and o.statedAt <= :threshold
    """)
	int updateStateBatch(@Param("current") OrderStatus current,
		@Param("threshold") java.time.LocalDateTime threshold,
		@Param("next") OrderStatus next);

	@Query(value = "select 'today' saledt, count(*) ordercnt, 0 totamt from Orders o"
		+ " where o.createdAt between concat(:saledt, ' 00:00:00.00') and concat(:saledt, ' 23:59:59.99')", nativeQuery = true)
	public SaleStat getTodayStat(@Param("saledt") String saledt);

	@Query(value =
		"select oi.item as id, max(oi.id) as orders, oi.item, sum(oi.cnt) as cnt, sum(oi.cnt * oi.price) as price"
			+ "  from Orders o inner join OrderItem oi on o.id = oi.orders"
			+ " where o.createdAt between concat(:saledt, ' 00:00:00.00') and concat(:saledt, ' 23:59:59.99')"
			+ " group by oi.item", nativeQuery = true)
	public List<OrderItems> getTodayItemStat(@Param("saledt") String saledt);

}