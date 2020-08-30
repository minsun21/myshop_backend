package com.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.domain.Continent;
import com.shop.domain.Image;

public interface ImageRepository extends JpaRepository<Image, Long>{

}
