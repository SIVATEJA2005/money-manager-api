package com.project.moneymanager;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MoneymanagerApplication {

	public static void main(String[] args) {
		// 1. Manually load .env variables
//		Dotenv dotenv = Dotenv.configure()
//				.directory("./") // points to project root
//				.ignoreIfMissing()
//				.load();
//
//		// 2. Set them as system properties so Spring can find them
//		dotenv.entries().forEach(entry -> {
//			System.setProperty(entry.getKey(), entry.getValue());
//		});

		SpringApplication.run(MoneymanagerApplication.class, args);
	}
}