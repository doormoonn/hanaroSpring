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

	@Query(value =
		"select oi.item_id as item, "
			+ "sum(oi.quantity) as quantity, "
			+ "sum(oi.quantity * i.price) as price "
			+ "from orders o inner join order_items oi on o.id = oi.order_id "
			+ "inner join item i on oi.item_id = i.id "
			+ "where o.created_at between concat(:saledt, ' 00:00:00.00') and concat(:saledt, ' 23:59:59.99') "
			+ "group by oi.item_id", nativeQuery = true)
	List<Object[]> getTodayItemStat(@Param("saledt") String saledt);


}