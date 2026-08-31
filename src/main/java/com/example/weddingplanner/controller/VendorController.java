package com.example.weddingplanner.controller;

import com.example.weddingplanner.dto.VendorRequest;
import com.example.weddingplanner.exception.ResourceNotFoundException;
import com.example.weddingplanner.model.Vendor;
import com.example.weddingplanner.model.VendorServiceType;
import com.example.weddingplanner.model.VendorStatus;
import com.example.weddingplanner.model.Wedding;
import com.example.weddingplanner.service.VendorService;
import com.example.weddingplanner.service.WeddingService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class VendorController {

    private final VendorService vendorService;
    private final WeddingService weddingService;

    public VendorController(VendorService vendorService, WeddingService weddingService){
        this.vendorService = vendorService;
        this.weddingService = weddingService;
    }

    @GetMapping("/vendors")
    public String showVendors(@RequestParam(required = false) VendorStatus status,
                              @RequestParam(required = false) VendorServiceType serviceType,
                              Model model){

        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()-> new IllegalStateException("Wedding not found."));

        List<Vendor> vendors;

        if(status != null && serviceType != null){
            vendors = vendorService.getVendorsByStatusAndServiceType(wedding.getId(), status, serviceType);
        }
        else if(status != null){
            vendors = vendorService.getVendorsByStatus(wedding.getId(), status);
        }
        else if(serviceType != null) {
            vendors = vendorService.getVendorsByServiceType(wedding.getId(), serviceType);
        }
        else{
            vendors = vendorService.getVendorsForWedding(wedding.getId());
        }

        model.addAttribute("vendors",vendors);
        model.addAttribute("vendorRequest",new VendorRequest());
        model.addAttribute("statuses", VendorStatus.values());
        model.addAttribute("serviceTypes", VendorServiceType.values());

        return"vendors";
    }

    @PostMapping("/vendors/save")
    public String saveVendor(@Valid @ModelAttribute("vendorRequest") VendorRequest vendorRequest,
                             BindingResult bindingResult,
                             Model model){

        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()-> new IllegalStateException("Wedding not found."));

        if(bindingResult.hasErrors()){
            model.addAttribute("vendors", vendorService.getVendorsForWedding(wedding.getId()));
            model.addAttribute("statuses", VendorStatus.values());
            model.addAttribute("serviceTypes", VendorServiceType.values());

            return "vendors";
        }

        Vendor vendor = new Vendor();

        vendor.setName(vendorRequest.getName());
        vendor.setServiceType(vendorRequest.getServiceType());
        vendor.setEmail(vendorRequest.getEmail());
        vendor.setPhone(vendorRequest.getPhone());
        vendor.setEstimatedPrice(vendorRequest.getEstimatedPrice());
        vendor.setDepositAmount(vendorRequest.getDepositAmount());
        vendor.setStatus(vendorRequest.getStatus());

        vendor.setWedding(wedding);

        vendorService.saveVendor(vendor);

        return "redirect:/vendors";
    }

    @GetMapping("/vendors/edit/{id}")
    public String editVendor(@PathVariable Long id, Model model){

        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()-> new IllegalStateException("Wedding not found."));

        Vendor vendor = vendorService.getVendorById(id, wedding.getId())
                .orElseThrow(()-> new ResourceNotFoundException("Vendor not found."));


        VendorRequest vendorRequest = new VendorRequest();

        vendorRequest.setId(vendor.getId());
        vendorRequest.setName(vendor.getName());
        vendorRequest.setServiceType(vendor.getServiceType());
        vendorRequest.setEmail(vendor.getEmail());
        vendorRequest.setPhone(vendor.getPhone());
        vendorRequest.setEstimatedPrice(vendor.getEstimatedPrice());
        vendorRequest.setDepositAmount(vendor.getDepositAmount());
        vendorRequest.setStatus(vendor.getStatus());

        model.addAttribute("vendorRequest", vendorRequest);
        model.addAttribute("statuses", VendorStatus.values());
        model.addAttribute("serviceTypes", VendorServiceType.values());

        return "vendor-edit";
    }

    @PostMapping("/vendors/update")
    public String updateVendor(@Valid @ModelAttribute("vendorRequest") VendorRequest vendorRequest,
                               BindingResult bindingResult,
                               Model model){

        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()-> new IllegalStateException("Wedding not found."));

        if(bindingResult.hasErrors()){
            model.addAttribute("statuses", VendorStatus.values());
            model.addAttribute("serviceTypes", VendorServiceType.values());

            return "vendor-edit";
        }

        Vendor existingVendor = vendorService.getVendorById(vendorRequest.getId(), wedding.getId())
                        .orElseThrow(()-> new ResourceNotFoundException("Vendor not found."));

        existingVendor.setName(vendorRequest.getName());
        existingVendor.setServiceType(vendorRequest.getServiceType());
        existingVendor.setEmail(vendorRequest.getEmail());
        existingVendor.setPhone(vendorRequest.getPhone());
        existingVendor.setEstimatedPrice(vendorRequest.getEstimatedPrice());
        existingVendor.setDepositAmount(vendorRequest.getDepositAmount());
        existingVendor.setStatus(vendorRequest.getStatus());

        vendorService.saveVendor(existingVendor);

        return"redirect:/vendors";
    }

    @PostMapping("/vendors/delete/{id}")
    public String deleteVendor(@PathVariable Long id){

        Wedding wedding = weddingService.getWedding()
                        .orElseThrow(()-> new IllegalStateException("Wedding not found."));

        vendorService.deleteVendor(id, wedding.getId());

        return "redirect:/vendors";
    }
}
