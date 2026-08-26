package com.example.weddingplanner.repository;

import com.example.weddingplanner.model.Wedding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeddingRepository extends JpaRepository<Wedding, Long> {
}
