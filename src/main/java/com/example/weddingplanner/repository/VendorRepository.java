package com.example.weddingplanner.repository;

import com.example.weddingplanner.model.Vendor;
import com.example.weddingplanner.model.VendorServiceType;
import com.example.weddingplanner.model.VendorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

    List<Vendor> findByWeddingId(Long weddingId);

    List<Vendor> findByWeddingIdAndStatus(Long weddingId, VendorStatus status);
    List<Vendor> findByWeddingIdAndServiceType(Long weddingId, VendorServiceType serviceType);

    List<Vendor> findByWeddingIdAndStatusAndServiceType(
            Long weddingId,
            VendorStatus status,
            VendorServiceType serviceType
    );

    Optional<Vendor> findByIdAndWeddingId(Long id, Long weddingId);
}
