package com.auth_service.controller;

import com.auth_service.dto.AuthResponse;
import com.auth_service.dto.LoginRequest;
import com.auth_service.dto.RegisterRequest;
import com.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Auth Controller - HTTP Request Handler
 *
 * Responsibilities:
 * 1. Receive HTTP requests
 * 2. Validate input (@Valid triggers validation annotations in DTOs)
 * 3. Call service layer
 * 4. Return HTTP response
 *
 * @RestController: Combines @Controller + @ResponseBody
 * @RequestMapping: Base path for all endpoints in this controller
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/health")
    public ResponseEntity<String> health(){
        return ResponseEntity.ok("Auth Service is running 🏃‍♂️‍➡️🏃‍♂️‍➡️");
    }

    /**
     * Register endpoint
     *
     * POST /register
     * Body: { "username": "john", "email": "john@example.com", "password": "pass123" }
     *
     * @Valid triggers validation (NotBlank, Email, Size, etc.)
     */

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request){
        try{
            AuthResponse response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error",e.getMessage()));
        }
    }

    /**
     * Login endpoint
     *
     * POST /login
     * Body: { "username": "john", "password": "pass123" }
     * Returns: { "token": "eyJhbGc...", "username": "john", ... }
     */

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error",e.getMessage()));
        }
    }

}
