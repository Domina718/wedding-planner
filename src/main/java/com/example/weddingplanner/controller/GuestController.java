package com.example.weddingplanner.controller;

import com.example.weddingplanner.dto.GuestRequest;
import com.example.weddingplanner.exception.ResourceNotFoundException;
import com.example.weddingplanner.model.Guest;
import com.example.weddingplanner.model.RsvpStatus;
import com.example.weddingplanner.model.Wedding;
import com.example.weddingplanner.service.GuestService;
import com.example.weddingplanner.service.WeddingService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GuestController {

    private final GuestService guestService;
    private final WeddingService weddingService;

    public GuestController(GuestService guestService, WeddingService weddingService){
        this.guestService = guestService;
        this.weddingService = weddingService;
    }

    @GetMapping("/guests")
    public String showGuests(@RequestParam(required = false) String search,
                             @RequestParam(required = false) RsvpStatus status,
                             Model model){

        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()->new IllegalStateException("Wedding not found."));

        List<Guest> guests;

        boolean hasSearch = search != null && !search.isBlank();

        if(hasSearch && status != null){

            guests = guestService.searchGuestsByStatus(wedding.getId(), search.trim(), status);
        } else if(hasSearch){

            guests = guestService.searchGuests(wedding.getId(), search.trim());
        } else if (status != null) {

            guests = guestService.getGuestsByStatus(wedding.getId(), status);
        } else{

            guests = guestService.getGuestsForWedding(wedding.getId());
        }

        model.addAttribute("guests", guests);
        model.addAttribute("guestRequest", new GuestRequest());
        model.addAttribute("rsvpStatuses", RsvpStatus.values());

        model.addAttribute("search", search);
        model.addAttribute("selectedStatus", status);

        return "guests";
    }

    @PostMapping("/guests/save")
    public String saveGuest(
            @Valid @ModelAttribute("guestRequest") GuestRequest guestRequest,
            BindingResult bindingResult,
            Model model){

        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()-> new IllegalStateException("Wedding not found."));

        if(bindingResult.hasErrors()){

            model.addAttribute("guests", guestService.getGuestsForWedding(wedding.getId()));
            model.addAttribute("rsvpStatuses", RsvpStatus.values());

            return "guests";
        }

        Guest guest = new Guest();

        guest.setFirstName(guestRequest.getFirstName());
        guest.setLastName(guestRequest.getLastName());
        guest.setEmail(guestRequest.getEmail());
        guest.setPhone(guestRequest.getPhone());
        guest.setRsvpStatus(guestRequest.getRsvpStatus());
        guest.setPlusOne(guestRequest.isPlusOne());
        guest.setPlusOneFirstName(guestRequest.getPlusOneFirstName());
        guest.setPlusOneLastName(guestRequest.getPlusOneLastName());

        guest.setWedding(wedding);

        guestService.saveGuest(guest);

        return "redirect:/guests";
    }

    @GetMapping("/guests/edit/{id}")
    public String editGuest(@PathVariable Long id, Model model){

        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()-> new IllegalStateException("Wedding not found."));

        Guest guest = guestService.getGuestById(id, wedding.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found"));

        GuestRequest guestRequest = new GuestRequest();

        guestRequest.setId(guest.getId());
        guestRequest.setFirstName(guest.getFirstName());
        guestRequest.setLastName(guest.getLastName());
        guestRequest.setEmail(guest.getEmail());
        guestRequest.setPhone(guest.getPhone());
        guestRequest.setRsvpStatus(guest.getRsvpStatus());
        guestRequest.setPlusOne(guest.isPlusOne());
        guestRequest.setPlusOneFirstName(guest.getPlusOneFirstName());
        guestRequest.setPlusOneLastName(guest.getPlusOneLastName());

        model.addAttribute("guestRequest", guestRequest);
        model.addAttribute("rsvpStatuses", RsvpStatus.values());

        return "guest-edit";
    }

    @PostMapping("/guests/update")
    public String updateGuest(@Valid @ModelAttribute ("guestRequest") GuestRequest guestRequest,
                              BindingResult bindingResult,
                              Model model){

        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()-> new IllegalStateException("Wedding not found."));

        if(bindingResult.hasErrors()){
            model.addAttribute("rsvpStatuses", RsvpStatus.values());
            return "guest-edit";
        }

        Guest existingGuest = guestService.getGuestById(guestRequest.getId(), wedding.getId())
                        .orElseThrow(()->new ResourceNotFoundException("Guest not found."));

        existingGuest.setFirstName(guestRequest.getFirstName());
        existingGuest.setLastName(guestRequest.getLastName());
        existingGuest.setEmail(guestRequest.getEmail());
        existingGuest.setPhone(guestRequest.getPhone());
        existingGuest.setRsvpStatus(guestRequest.getRsvpStatus());
        existingGuest.setPlusOne(guestRequest.isPlusOne());
        existingGuest.setPlusOneFirstName(guestRequest.getPlusOneFirstName());
        existingGuest.setPlusOneLastName(guestRequest.getPlusOneLastName());

        guestService.saveGuest(existingGuest);

        return "redirect:/guests";
    }

    @PostMapping("/guests/delete/{id}")
    public String deleteGuest(@PathVariable Long id){

        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()-> new IllegalStateException("Wedding not found."));

        guestService.deleteGuest(id, wedding.getId());

        return "redirect:/guests";
    }

}
