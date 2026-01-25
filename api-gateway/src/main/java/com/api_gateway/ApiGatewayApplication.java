package com.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * API Gateway - The Front Door
 *
 * Single entry point for all client requests.
 * Responsibilities:
 * 1. Route requests to appropriate microservices
 * 2. Validate JWT tokens
 * 3. Load balance between multiple instances
 * 4. Handle CORS
 */

@SpringBootApplication
@EnableDiscoveryClient // Register with Eureka and discover other services
public class ApiGatewayApplication {

	public static void main(String[] args) {

		SpringApplication.run(ApiGatewayApplication.class, args);
		System.out.println("Api Gateway started at http://localhost:8080");
	}

}
