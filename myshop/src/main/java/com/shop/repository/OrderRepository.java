package com.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.domain.Order;
import com.shop.domain.OrderItem;
import com.shop.domain.enums.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByStatus(OrderStatus status);
	List<Order> findByOrderItems(List<OrderItem> orderItems);
	
}
