package com.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Authentication Response
 * Return JWT token to client after successful login
 * */

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private String email;
    private String message;
}
