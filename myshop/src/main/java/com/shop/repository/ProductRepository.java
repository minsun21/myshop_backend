package com.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.domain.Continent;
import com.shop.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
