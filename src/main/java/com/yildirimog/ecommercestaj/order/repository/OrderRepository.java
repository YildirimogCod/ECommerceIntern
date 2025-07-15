package com.yildirimog.ecommercestaj.order.repository;

import com.yildirimog.ecommercestaj.common.enums.OrderStatus;
import com.yildirimog.ecommercestaj.order.entity.Order;
import com.yildirimog.ecommercestaj.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId")
    List<Order> findByUserId(@Param("userId") Long userId);
    @Query("SELECT o FROM Order o WHERE o.user = :user AND o.status IN :statuses")
    List<Order> findByUserAndStatusIn(User user, List<OrderStatus> paid);
}
