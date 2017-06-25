package com.staroot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;




@SpringBootApplication(scanBasePackages = {StarootApplication.BASE_PACKAGES})
@EnableAutoConfiguration
@EnableTransactionManagement
public class StarootApplication {
	public static final String BASE_PACKAGES = "com.staroot";
	public static String UPLOAD_DIR = "/staroot_upload_dir";
	public static String UPLOAD_DIR_PATH;

	public static void main(String[] args) {
		SpringApplication.run(StarootApplication.class, args);
	}
}
