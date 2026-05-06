package com.fooddelivery.repository;

import com.fooddelivery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * UserRepository - Database operations for User
 * Component 1: IT251022022
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // READ - Find by email (for login)
    Optional<User> findByEmail(String email);

    // READ - Check if email exists (duplicate check on register)
    boolean existsByEmail(String email);

    // READ - Search users by name or email
    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%',:term,'%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%',:term,'%'))")
    List<User> searchUsers(@Param("term") String term);

    // READ - Find all customers only
    List<User> findByRole(String role);
}
