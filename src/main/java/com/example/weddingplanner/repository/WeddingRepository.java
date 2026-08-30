package com.example.weddingplanner.repository;

import com.example.weddingplanner.model.User;
import com.example.weddingplanner.model.Wedding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeddingRepository extends JpaRepository<Wedding, Long> {

    Optional<Wedding> findByUser(User user);
}
