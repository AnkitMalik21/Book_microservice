package com.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security Configuration
 *
 * WHY: Spring Security blocks everything by default
 * We need to allow /register and /login without authentication
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Password Encoder Bean
     *
     * BCrypt: One-way hashing algorithm
     * - Same password → different hash each time (salt)
     * - Cannot reverse hash to get password
     * - Slow by design (prevents brute force)
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Security Filter Chain
     *
     * Configure which endpoints require authentication
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf ->csrf.disable())
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/register","/login","/health").permitAll() //public endPoints
                        .anyRequest().authenticated() //All other endpoints need auth
                )
                .httpBasic(httpBasic->httpBasic.disable())
                .formLogin(form->form.disable());

        return http.build();
    }
}
