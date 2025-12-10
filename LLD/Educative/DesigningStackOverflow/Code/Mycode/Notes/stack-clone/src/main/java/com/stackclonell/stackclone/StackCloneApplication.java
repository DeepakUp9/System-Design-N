package com.stackclonell.stackclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching // Enable Spring's Caching Abstraction
public class StackCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(StackCloneApplication.class, args);
		System.out.println("Stack Clone Application is running. Check /actuator/health");
	}

}
