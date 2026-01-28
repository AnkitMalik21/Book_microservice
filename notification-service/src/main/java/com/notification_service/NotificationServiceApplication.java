package com.notification_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient //This helps it to register to eureka
public class NotificationServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(NotificationServiceApplication.class, args);
		System.out.println("🔔 Notification Service started at http://localhost:8084");
		System.out.println("📬 Listening for order events on Kafka topic: order-events");
	}

}
