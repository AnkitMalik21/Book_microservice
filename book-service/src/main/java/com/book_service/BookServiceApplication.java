package com.book_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


/**
 * Book Service - Complete Microservice
 *
 * Features:
 * ✅ Full CRUD for Books
 * ✅ Own PostgreSQL database
 * ✅ Registers with Eureka
 * ✅ Secured (trusts Gateway JWT via X-User-Id header)
 * ✅ Health endpoints
 * ✅ Production logging
 *
 * @EnableDiscoveryClient: Registers with Eureka automatically
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableWebSecurity // Enable minimal security
public class BookServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(BookServiceApplication.class, args);
		System.out.println("""
            📚 BOOK SERVICE STARTED SUCCESSFULLY!
            ┌─────────────────────────────────────┐
            │ Port: 8082                         │
            │ Eureka: http://localhost:8761      │
            │ Health: http://localhost:8082/actuator/health │
            │ Books: http://localhost:8082/books │
            └─────────────────────────────────────┘
            """);
	}

}
