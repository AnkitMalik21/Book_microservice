package com.auth_service.repository;

import com.auth_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User Repository - Database Access Layer
 *
 * JpaRepository provides CRUD operations out of the box:
 * - save(), findById(), findAll(), delete(), etc.
 *
 * Custom methods follow naming convention:
 * findBy<FieldName> → Spring generates SQL automatically
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    // Spring generates: Select * from users where username = ?
    Optional<User> findByUsername(String username);

    // Spring generates: SELECT * from users where email = ?
    Optional<User> findByEmail(String email);

    // Spring generates: SELECT EXIST(SELECT 1 users WHERE username = ?)
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
