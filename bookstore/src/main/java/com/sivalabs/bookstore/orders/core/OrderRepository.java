package com.sivalabs.bookstore.orders.core;

import com.sivalabs.bookstore.orders.core.models.OrderSummary;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderId(String orderId);

    List<Order> findByStatus(OrderStatus status);

    @Query(
            "select new com.sivalabs.bookstore.orders.core.models.OrderSummary(o.id, o.orderId, o.status, o.createdAt) from Order o")
    List<OrderSummary> findAllOrderSummaries(Sort sort);
}
