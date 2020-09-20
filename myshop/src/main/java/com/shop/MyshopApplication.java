package com.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class MyshopApplication {
	public static void main(String[] args) {
		log.info("<><><><><><><><><><><><><><><><><><>start");
		SpringApplication.run(MyshopApplication.class, args);
	}

}
