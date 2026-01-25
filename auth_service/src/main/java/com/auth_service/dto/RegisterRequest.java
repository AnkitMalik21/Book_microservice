package com.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Registration Request DTO
 *
 * WHY DTO? Never expose Entity directly to controllers.
 * Benefits:
 * 1. Security - hide internal structure
 * 2. Validation - validate at controller layer
 * 3. Flexibility - different DTOs for different operations
 */

@Data
public class RegisterRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3,max = 50,message = "Username must be 3-50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6,message = "Password must be at least 6 characters")
    private String password;
}
