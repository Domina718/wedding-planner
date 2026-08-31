package com.example.weddingplanner.service;

import com.example.weddingplanner.model.Vendor;
import com.example.weddingplanner.model.VendorServiceType;
import com.example.weddingplanner.model.VendorStatus;
import com.example.weddingplanner.repository.VendorRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class VendorServiceImpl implements VendorService{

    private final VendorRepository vendorRepository;

    public VendorServiceImpl(VendorRepository vendorRepository){
        this.vendorRepository = vendorRepository;
    }

    @Override
    public List<Vendor> getVendorsForWedding(Long weddingId){
        return vendorRepository.findByWeddingId(weddingId);
    }

    @Override
    public List<Vendor> getVendorsByStatus(Long weddingId, VendorStatus status){
        return vendorRepository.findByWeddingIdAndStatus(weddingId, status);
    }

    @Override
    public List<Vendor> getVendorsByServiceType(Long weddingId, VendorServiceType serviceType){
        return vendorRepository.findByWeddingIdAndServiceType(weddingId, serviceType);
    }

    @Override
    public List<Vendor> getVendorsByStatusAndServiceType(Long weddingId, VendorStatus status, VendorServiceType serviceType){
        return vendorRepository.findByWeddingIdAndStatusAndServiceType(weddingId, status, serviceType);
    }

    @Override
    public Optional<Vendor> getVendorById(Long id, Long weddingId){
        return vendorRepository.findByIdAndWeddingId(id, weddingId);
    }

    @Override
    public Vendor saveVendor(Vendor vendor){
        return vendorRepository.save(vendor);
    }

    @Override
    public void deleteVendor(Long id, Long weddingId){
        vendorRepository.findByIdAndWeddingId(id, weddingId).ifPresent(vendorRepository::delete);
    }

    @Override
    public long countVendors(Long weddingId){
        return vendorRepository.findByWeddingId(weddingId).size();
    }

    @Override
    public long countBookedVendors(Long weddingId){
        return vendorRepository.findByWeddingId(weddingId)
                .stream()
                .filter(vendor -> vendor.getStatus() == VendorStatus.BOOKED
                    || vendor.getStatus() == VendorStatus.PAID)
                .count();
    }

    @Override
    public BigDecimal getTotalRemainingAmount(Long weddingId){
        return vendorRepository.findByWeddingId(weddingId)
                .stream()
                .map(Vendor::getRemainingAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
