package com.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.domain.Item;
import com.shop.domain.enums.ItemStatus;


public interface ItemRepository extends JpaRepository<Item, Long>{
	List<Item> findbyStatus(ItemStatus status);
}
