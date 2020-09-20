package com.shop.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.tomcat.jni.Address;

import com.shop.domain.enums.ROLE;

import lombok.Getter;

@Getter
@Table(name = "member")
@Entity
public class Member {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long memberId;
	
	private String email;
	
	private String password;
	
	private String name;
	
	private String phone;
	
	@Embedded
	private Address address;
	
	@OneToMany(mappedBy="member")
	private List<Order> orders = new ArrayList<>();
	
	@Enumerated(EnumType.STRING)
	private ROLE role;
}
