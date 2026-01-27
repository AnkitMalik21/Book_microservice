package com.book_service.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * Security Context Filter
 *
 * WHY?
 * - Gateway validates JWT and passes X-User-Id + X-User-Role headers
 * - This filter populates SecurityContext from headers
 * - Enables @PreAuthorize to work
 *
 * Flow:
 * 1. Extract headers from request
 * 2. Create Authentication object with role
 * 3. Set in SecurityContext
 * 4. @PreAuthorize can now check roles
 */

@Component
public class SecurityContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Extract headers passed by Gateway
        String userId = request.getHeader("X-User-Id");
        String userRole = request.getHeader("X-User-Role");

        if(userId != null && userRole != null){
            // create authority with ROLE_ prefix (Spring Security convention)
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_"+userRole);

            //create authentication object
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userId, //Principal (username)
                    null,
                    Collections.singletonList(authority) // Authorities (roles)
            );

            // Set in Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            System.out.println("✅ SecurityContext populated: User=" + userId + ",Role="+userRole);
        }

        filterChain.doFilter(request,response);
    }
}
