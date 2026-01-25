package com.auth_service.repository;

import com.auth_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
    Optional generated
}
