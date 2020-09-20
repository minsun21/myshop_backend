package com.shop.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;

@Getter
@Table(name = "product")
@Entity
public class Product {
	@Column(name = "product_id")
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	private String title;

	private String description;

	private int price;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<Image> images = new ArrayList<>();

	private int sold;

	private int views;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "continent_id")
	private Continent continent;

	public void addImage(Image image) {
		images.add(image);
		image.insertProduct(this);
	}
	
	@Builder
	public Product(Long productId, Member member, String title, String description, int price, int sold, int views,
			Continent continent) {
		this.productId = productId;
		this.member = member;
		this.title = title;
		this.description = description;
		this.price = price;
		this.sold = sold;
		this.views = views;
		this.continent = continent;
	}
	
}
