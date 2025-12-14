package com.stockbrokerage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync // Crucial for non-blocking event handling
public class OnlineStockBrokerageSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineStockBrokerageSystemApplication.class, args);
	}

}
