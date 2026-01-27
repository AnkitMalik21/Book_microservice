package com.api_gateway.config;

import com.api_gateway.filter.JwtAuthenticationFilter;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Gateway Route Configuration
 *
 * Defines how requests are routed to microservices.
 *
 * Route Pattern:
 * 1. Match path (predicate)
 * 2. Apply filters (JWT validation, rewrite path)
 * 3. Route to service via Eureka (lb://)
 *
 * Example:
 * GET /api/books/123 → lb://BOOK-SERVICE/books/123
 */

@Component
public class GatewayConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder){
        return builder.routes()
                // ==== AUTH SERVICE (No JWT Required) =====
                .route("auth-service",r->r
                        .path("/auth/**")
                        .filters(f->f
                                //Remove /auth prefix before forwarding
                                // /auth/login -> /login
                                .rewritePath("/auth/(?<segment>.*)","/${segment}")
                        )
                        .uri("lb://AUTH-SERVICE") // Load-balanced via Eureka
                )

                // ===== BOOK SERVICE (JWT Required)
                .route("book-service",r->r
                        .path("/api/books/**")
                        .filters(f->f
                                //Remove /api/books prefix
                                // /api/books/123 -> /books/123
                                .rewritePath("/api/books/(?<segment>.*)","/books/${segment}")
                                // Apply JWT validation filter
                                .filter(jwtAuthenticationFilter)
                        )
                        .uri("lb://BOOK-SERVICE")
                )

                // ==== order service (JWT Required)
                .route("order-service",r->r
                        .path("/api/orders/**")
                        .filters(f->f
                                //Remove /api/orders prefix
                                // /api/orders/my-orders-> /orders/my orders
                                .rewritePath("/api/orders/(?<segment>.*)",
                                        "/orders/${segment}"
                                )
                                //Apply JWT validation filter
                                .filter(jwtAuthenticationFilter)
                        )
                       .uri("lb://ORDER-SERVICE")
                ).build();

    }
}
