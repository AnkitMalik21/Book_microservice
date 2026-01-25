package com.api_gateway.filter;

import com.api_gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * JWT Authentication Filter
 *
 * This filter runs BEFORE routing to microservices.
 * Think of it as a security guard checking IDs before letting people enter.
 *
 * Flow:
 * 1. Request arrives at gateway
 * 2. This filter checks for JWT in Authorization header
 * 3. If valid → add username to request header → forward to service
 * 4. If invalid → return 401 Unauthorized
 */

@Component
public class JwtAuthenticationFilter implements GatewayFilter {

    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        //Extract Authorization header
        if(!request.getHeaders().containsKey("Authorization")){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String authHeader = request.getHeaders().get("Authorization").get(0);

        // Check if header starts with "Bearer "
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Extract token (remove "Bearer " prefix)
        String token = authHeader.substring(7);

        //validate Token
        if(!jwtUtil.validationToken(token)){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        //Extract username and add ti request header
        //Why? so downstream services know WHO is making the request
        String username = jwtUtil.extractUsername(token);
        ServerHttpRequest modifiedRequest = request.mutate()
                .header("X-User-Id",username)
                .build();

        // Forward to next filter/service
        return chain.filter(exchange.mutate().request(modifiedRequest).build());

    }
}
