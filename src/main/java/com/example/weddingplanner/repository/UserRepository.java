package com.example.weddingplanner.repository;

import com.example.weddingplanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail (String email);

    boolean existsByEmail(String email);
}
