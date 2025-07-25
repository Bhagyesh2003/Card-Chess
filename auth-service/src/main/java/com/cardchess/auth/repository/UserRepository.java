package com.cardchess.auth.repository;

import com.cardchess.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
// Repository interface for User entity
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}