package com.order_service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }
}

/**
  In simple terms: It sets up a "smart" tool that lets your application
  talk to other microservices by name, rather than by exact address.
 */

/**
How it works (The Analogy)
Imagine you are in a large office building (the Cloud). You want to speak
to someone in the "HR Department."

Standard WebClient: You have to know that HR is currently sitting at Desk #402.
You walk to Desk #402. If they move to Desk #500, you are lost.

@LoadBalanced WebClient:
You just tell the building receptionist (the Load Balancer),
"Connect me to HR Department."
The receptionist looks up where HR is currently sitting.
If there are three HR employees, the receptionist picks one for you
automatically (balancing the load so one person isn't overwhelmed).

You never need to know the desk number (IP address), just the department name (Service ID).

 */


/**
 * WebClient.Builder: WebClient is the modern tool in Java for making HTTP requests
 *  (like sending a GET or POST request to another server). We are creating a "Builder"
 *  here, which is a factory used to create the actual client later.
 *  */


/**
The "Magic" Part: @LoadBalanced
This is the most important line. Without this annotation,
this is just a standard HTTP client. With this annotation, it becomes "smart."

Without @LoadBalanced: If you want to call an API (e.g., an Inventory Service),
you have to know exactly where it lives. webClient.get().uri("http://192.168.1.55:8080/items")

Problem: If the Inventory Service moves to a different IP address, your code breaks.
With @LoadBalanced: You don't use IP addresses. You use the name of the service as
it is registered in your Service Discovery (like Eureka or Consul).
webClient.get().uri("http://inventory-service/items")


 */
