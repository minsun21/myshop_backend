package com.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.domain.Item;
import com.shop.domain.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
