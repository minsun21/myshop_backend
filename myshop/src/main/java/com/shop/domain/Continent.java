package com.shop.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name ="continent")
@Entity
public class Continent {
	
	public Continent(String name) {
		this.name = name;
	}
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="continent_id")
	private Long continentId;
	
	private String name;
	
}
