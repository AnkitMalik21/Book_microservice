package com.book_service.config;

import com.book_service.security.SecurityContextFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security Configuration with Method-Level Security
 *
 * @EnableMethodSecurity: Activates @PreAuthorize, @PostAuthorize, @Secured
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // ✅ Enable @PreAuthorize
public class SecurityConfig {

    @Autowired
    private SecurityContextFilter securityContextFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/actuator/health","/health","/books/health")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form->form.disable())
                .httpBasic(basic->basic.disable())

                // ✅ Add custom filter BEFORE Spring Security processes
                .addFilterBefore(securityContextFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
