package com.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Security Configuration for Gateway

 * WHY: Spring Security is enabled by default, blocking all requests.
 * We need to configure which routes require authentication.
 */

@Configuration
@EnableWebFluxSecurity // Reactive security (Gateway is reactive/non-blocking)
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        http
                .csrf(csrf->csrf.disable())
                .authorizeExchange(exchange -> exchange
                        //Public endpoints (no authentication)
                        .pathMatchers("/auth/**").permitAll() //Allow auth endpoints without token
                        .pathMatchers("/eureka/**").permitAll() //Allow Eureka dashboard
                        .pathMatchers("/actuator/**").permitAll()

                        //All other routes need authentication
                        // (JwtsAuthenticationFilter handles validation)
                        .anyExchange().authenticated()
                )
                //Disable form login & HTTP basic auth
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable);

        return http.build();
    }
}
