package com.shop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.domain.Continent;
import com.shop.domain.TripProduct;

public interface TripProductRepository extends JpaRepository<TripProduct, Long>{
//	Page<Product> findAll(Pageable pageable);
	List<TripProduct> findAll();
//	Page<Product> findByContinent(Continent continent, Pageable pageable);
	TripProduct findByContinent(Continent continent);
}
