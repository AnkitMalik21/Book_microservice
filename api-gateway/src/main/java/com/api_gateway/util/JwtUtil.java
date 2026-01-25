package com.api_gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT Utility Class
 *
 * Handles JWT validation at the gateway level.
 * WHY: We validate once at gateway instead of every service validating separately.
 *
 * How JWT works:
 * 1. User logs in → Auth Service creates JWT (encoded user info)
 * 2. User sends JWT with every request
 * 3. Gateway validates signature (ensures not tampered)
 * 4. If valid, forward to service; if invalid, reject
 */

@Component
public class JwtUtil {

    // Secret key must be at least 256 bits (32 characters)
    @Value("${jwt.secret}")
    private String secret;

    /**
     Get Signing key from secret String
     */
    private SecretKey getSigningKey(){
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Validate JWT token
     * Checks:
     * 1. Signature is valid (not tampered)
     * 2. Token not expired
     * 3. Claims are present
     */

    public boolean validationToken(String token){
        try{
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            System.out.println("❌ Invalid JWT: " + e.getMessage());
            return false;
        }
    }

    /**
     *Extract all claims from token
     *Claims = user info stored in JWT (username, roles,etc.)
     */
    public Claims getAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //check if token is expired
    public boolean isTokenExpired(String token){
        return getAllClaims(token).getExpiration().before(new Date());
    }

    //Extract username from token
    public String extractUsername(String token){
        return getAllClaims(token).getSubject();
    }

}
