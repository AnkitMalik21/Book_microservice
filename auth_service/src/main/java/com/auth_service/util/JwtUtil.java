package com.auth_service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT Utility - Token Generation and Validation
 *
 * JWT Structure: header.payload.signature
 * Example: eyJhbGc...(header).eyJzdWI...(payload).SflKxwRJ...(signature)
 *
 * Payload contains:
 * - sub: subject (username)
 * - iat: issued at
 * - exp: expiration
 * - custom claims (role, email, etc.)
 */

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    private SecretKey getSigningKey(){
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generate JWT Token
     *
     * @param username - User's username
     * @return JWT token string
     */
    public String generateToken(String username){
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims,username);
    }

    /**
     * Generate tokn with custom claims
     * */

    public String generateToken(String username,String email,String role){
        Map<String,Object> claims = new HashMap<>();
        claims.put("email",email);
        claims.put("role",role);
        return createToken(claims,username);
    }

    private String createToken(Map<String,Object> claims,String subject){
        Date now = new Date();
        Date expirayDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(claims)
                .subject(subject) //Username
                .issuedAt(now)
                .expiration(expirayDate)
                .signWith(getSigningKey())
                .compact();

    }

    /**
     * Validate token
     */
    public boolean validateToken(String token){
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extract Username from token
     */
    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token){
        return (String) extractAllClaims(token).get("role");
    }

    private Claims extractAllClaims(String token){
         return Jwts.parser()
                 .verifyWith(getSigningKey())
                 .build()
                 .parseSignedClaims(token)
                 .getPayload();
    }
}
