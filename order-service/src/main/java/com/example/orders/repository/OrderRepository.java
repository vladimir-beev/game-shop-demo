package com.example.orders.repository;

import com.example.orders.entity.Order;
import com.example.orders.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUserIdAndStatus(String userId, OrderStatus status);
    List<Order> findByUserIdAndStatusIn(String userId, List<OrderStatus> statuses);
}
