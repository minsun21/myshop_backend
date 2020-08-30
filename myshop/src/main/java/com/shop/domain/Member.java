package com.shop.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "member")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Member {
	@Column(name = "member_id")
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;
	
	private String email;
	
	private String password;
	
	private String name;
	
	private String phone;
	
	@Enumerated(EnumType.STRING)
	private ROLE role;
}
