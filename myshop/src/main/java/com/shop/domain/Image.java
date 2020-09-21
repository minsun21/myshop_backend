package com.shop.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.shop.domain.enums.IMAGE_TYPE;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Table(name ="image")
@Entity
public class Image {
	
	@Column(name="image_id")
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long imagesId;
	
	private String path;
	
	private IMAGE_TYPE type;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="product_id")
	private TripProduct product;
	
	protected void insertProduct(TripProduct product) {
		this.product = product;
	}
}
