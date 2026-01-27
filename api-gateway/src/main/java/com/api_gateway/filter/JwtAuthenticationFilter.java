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

        // Step 1: Extract Authorization header
        // Check if Authorization header exists
        if(!request.getHeaders().containsKey("Authorization")){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }



        //Step 2: Extract Authorization header
        String authHeader = request.getHeaders().get("Authoirzation").get(0);

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }


        //Step 3: Extract token (remove "Bearer " prefix)
        String token = authHeader.substring(7);


        //Step 4: Validation token
        if(!jwtUtil.validateToken(token)){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        //Step 5: Extract username and role
        String username = jwtUtil.extractUsername(token);
        String role = jwtUtil.extractRole(token); //Extract role

        //Step 6 : Add headers for downstream services
        ServerHttpRequest modifiedRequest = request.mutate()
                .header("X-User-Id",username) //Username for audit trail
                .header("X-User-Role", role) // Pass role
                .build();

        //Step 7: Forward request with modified headers
        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }
}
