package com.auth_service.service;


import com.auth_service.dto.AuthResponse;
import com.auth_service.dto.LoginRequest;
import com.auth_service.dto.RegisterRequest;
import com.auth_service.entity.User;
import com.auth_service.repository.UserRepository;
import com.auth_service.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
/**
 * Auth Service - Business Logic Layer
 *
 * WHY Service Layer?
 * - Controllers handle HTTP, Services handle business logic
 * - Keeps controllers thin, services testable
 * - Single Responsibility Principle
 *
 * @Transactional: If method fails midway, rollback database changes
 */


 @Service
 @RequiredArgsConstructor
 public class AuthService {

     private final UserRepository userRepository;
     private final PasswordEncoder passwordEncoder;
     private final JwtUtil jwtUtil;

    /**
     * Register new user
     *
     * Steps:
     * 1. Check if username/email exists
     * 2. Hash password (NEVER store plain text!)
     * 3. Save to database
     * 4. Generate JWT token
     * 5. Return token
     */
    @Transactional
    public AuthResponse register(RegisterRequest request){
        //Validation
        if(userRepository.existsByUsername(request.getUsername())){
            throw new RuntimeException("Username already exists");
        }

        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists");
        }

        //Create user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); //Hash password
        user.setRole("USER");

        //Save to database
        User savedUser = userRepository.save(user);

        //Generate JWT
        String token = jwtUtil.generateToken(
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole()
        );

        return new AuthResponse(
                token,
                savedUser.getUsername(),
                savedUser.getEmail(),
                "Registration Successful"
        );
    }

    /**
     * Login user
     *
     * Steps:
     * 1. Find user by username
     * 2. Verify password (compare hash)
     * 3. Generate JWT token
     * 4. Return token
     */

    public AuthResponse login(LoginRequest request){
        // Find user
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(()-> new RuntimeException("Invalid Username"));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid username or password");
        }

        // Generate JWT
        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );

        return new AuthResponse(
                token,
                user.getUsername(),
                user.getEmail(),
                "Login successful"
        );
    }

 }
