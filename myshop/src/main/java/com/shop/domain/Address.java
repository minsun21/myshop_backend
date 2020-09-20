package com.shop.domain;

import javax.persistence.Embeddable;

import lombok.Builder;
import lombok.Getter;

@Builder
@Embeddable
@Getter
public class Address {
	private String city;

	private String street;

	private String zipcode;
}
