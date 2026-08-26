package com.example.weddingplanner.service;

import com.example.weddingplanner.model.Vendor;
import com.example.weddingplanner.model.VendorServiceType;
import com.example.weddingplanner.model.VendorStatus;

import java.util.List;
import java.util.Optional;

public interface VendorService {

    List<Vendor> getVendorsForWedding(Long weddingId);

    List<Vendor> getVendorsByStatus(Long weddingId, VendorStatus status);

    List<Vendor> getVendorsByServiceType(Long weddingId, VendorServiceType serviceType);

    List<Vendor> getVendorsByStatusAndServiceType(Long weddingId, VendorStatus status, VendorServiceType serviceType);

    Optional<Vendor> getVendorById(Long id);

    Vendor saveVendor(Vendor vendor);

    void deleteVendor(Long id);

    long countVendors(Long weddingId);

    long countBookedVendors(Long weddingId);


}
