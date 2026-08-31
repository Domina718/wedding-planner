package com.example.weddingplanner.repository;

import com.example.weddingplanner.model.Guest;
import com.example.weddingplanner.model.RsvpStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

    List<Guest> findByWeddingId(Long weddingId);

    Optional<Guest> findByIdAndWeddingId(Long id, Long weddingId);

    List<Guest> findByWeddingIdAndRsvpStatus(Long weddingId, RsvpStatus rsvpStatus);


    @Query("""
            SELECT g
            FROM Guest g
            WHERE g.wedding.id = :weddingId
              AND (
                LOWER(g.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(g.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(g.plusOneFirstName) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(g.plusOneLastName) LIKE LOWER(CONCAT('%', :search, '%'))
             )              
           """)

    List<Guest> searchByWedding(
            @Param("weddingId") Long weddingId,
            @Param("search") String search
    );

    @Query("""
            SELECT g
            FROM Guest g
            WHERE g.wedding.id = :weddingId
              AND g.rsvpStatus = :status
              AND (
                LOWER(g.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(g.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(g.plusOneFirstName) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(g.plusOneLastName) LIKE LOWER(CONCAT('%', :search, '%'))
             )              
           """)

    List<Guest> searchByWeddingAndStatus(
            @Param("weddingId") Long weddingId,
            @Param("status") RsvpStatus status,
            @Param("search") String search
    );

}
