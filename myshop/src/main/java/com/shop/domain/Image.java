package com.shop.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@Table(name ="image")
@Entity
public class Image {
	
	@Column(name="image_id")
	@Id @GeneratedValue
	private Long imagesId;
	
	private String path;
	
	private IMAGE_TYPE type;
	
	@ManyToOne
	@JoinColumn(name="product_id")
	private Product product;
}
