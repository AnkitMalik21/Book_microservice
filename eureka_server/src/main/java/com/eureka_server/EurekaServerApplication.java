package com.eureka_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka Server - The Service Registry
 *
 * Think of this as a phonebook for microservices.
 * When services start, they register here with their name and address.
 * Other services query this to find each other.
 *
 * @EnableEurekaServer: Tells Spring Boot to run Eureka Server
 */

@SpringBootApplication
@EnableEurekaServer // This single annotation makes it a Eureka Server
public class EurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
	}

}
