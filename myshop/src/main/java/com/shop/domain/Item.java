package com.shop.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.shop.domain.enums.ItemStatus;

import lombok.Getter;

@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "item")
@Entity
public class Item {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_id")
	private Long id;
	 
	private String name;
	 
	private int price;
	 
	private int stockQuantity;
	
	private LocalDateTime paymentDate;
	
	private LocalDateTime cancelDate;
	
	@OneToMany(mappedBy = "item")
	private List<CategoryItem> categories = new ArrayList<CategoryItem>();
	
	@Enumerated(EnumType.STRING)
	private ItemStatus status;
	
	public void modifiedStatus(ItemStatus status) {
		this.status = status;
	}

}
