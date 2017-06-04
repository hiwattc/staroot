package com.staroot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {starootApplication.BASE_PACKAGES})
@EnableAutoConfiguration
@EnableTransactionManagement
public class starootApplication {
	public static final String BASE_PACKAGES = "com.staroot";

	public static void main(String[] args) {
		SpringApplication.run(starootApplication.class, args);
	}
}
