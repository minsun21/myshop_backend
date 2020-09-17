package com.shop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.domain.Continent;
import com.shop.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
//	Page<Product> findAll(Pageable pageable);
	List<Product> findAll();
	Page<Product> findByContinent(Continent continent, Pageable pageable);
}
