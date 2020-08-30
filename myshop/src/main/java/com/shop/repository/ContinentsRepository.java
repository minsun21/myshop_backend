package com.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.domain.Continent;

public interface ContinentsRepository extends JpaRepository<Continent, Long>{

}
