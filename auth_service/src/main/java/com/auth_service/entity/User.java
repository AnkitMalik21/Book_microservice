package com.auth_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * User Entity - Represents users table in database
 *
 * WHY: Store user credentials for authentication
 *
 * @Entity: Tells JPA this is a database table
 * @Table: Specifies table name (default would be 'user' which is reserved keyword in PostgreSQL)
 * @Data: Lombok generates getters, setters, toString, equals, hashCode
 */

@Entity
@Table(name="users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String username;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;  // Stored as BCrypt hash, never plain text!

    @Column(nullable = false)
    private String role = "USER"; //Default role

    @Column(name="created_at",nullable=false,updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PreUpdate
    public void preUpdate(){
        updatedAt = LocalDateTime.now();
    }

    /**
     * @PreUpdate: This is a Lifecycle Callback annotation. It acts like a "hook" or a "trigger."
       When it runs: Hibernate listens for when you are about to save changes to an existing entity
       (an SQL UPDATE operation). Just before it sends that SQL to the database,
       it runs this method.
     */

}
