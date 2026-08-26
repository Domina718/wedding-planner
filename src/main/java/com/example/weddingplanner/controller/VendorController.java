package com.example.weddingplanner.controller;

import com.example.weddingplanner.model.Vendor;
import com.example.weddingplanner.model.VendorServiceType;
import com.example.weddingplanner.model.VendorStatus;
import com.example.weddingplanner.model.Wedding;
import com.example.weddingplanner.service.VendorService;
import com.example.weddingplanner.service.WeddingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
                .orElseThrow(()-> new IllegalStateException("Wedding has not been created yet."));

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
        model.addAttribute("vendor",new Vendor());
        model.addAttribute("statuses", VendorStatus.values());
        model.addAttribute("serviceTypes", VendorServiceType.values());

        return"vendors";
    }

    @PostMapping("/vendors/save")
    public String saveVendor(@ModelAttribute Vendor vendor){

        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()-> new IllegalStateException("Wedding has not been created yet."));

        vendor.setWedding(wedding);
        vendorService.saveVendor(vendor);

        return "redirect:/vendors";
    }

    @GetMapping("/vendors/edit/{id}")
    public String editVendor(@PathVariable Long id, Model model){
        Vendor vendor = vendorService.getVendorById(id)
                .orElseThrow(()-> new IllegalArgumentException("Vendor not found."));

        model.addAttribute("vendor", vendor);
        model.addAttribute("statuses", VendorStatus.values());
        model.addAttribute("serviceTypes", VendorServiceType.values());

        return "vendor-edit";
    }

    @PostMapping("/vendors/update")
    public String updateVendor(@ModelAttribute Vendor vendor){
        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()-> new IllegalStateException("Wedding has not been created yet."));

        vendor.setWedding(wedding);
        vendorService.saveVendor(vendor);

        return"redirect:/vendors";
    }

    @PostMapping("/vendors/delete/{id}")
    public String deleteVendor(@PathVariable Long id){
        vendorService.deleteVendor(id);

        return "redirect:/vendors";
    }
}
