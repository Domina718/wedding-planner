package com.example.weddingplanner.repository;

import com.example.weddingplanner.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

    List<Guest> findByWeddingId(Long weddingId);

    Optional<Guest> findByIdAndWeddingId(Long id, Long weddingId);
}
