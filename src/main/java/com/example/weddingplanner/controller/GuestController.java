package com.example.weddingplanner.controller;

import com.example.weddingplanner.model.Guest;
import com.example.weddingplanner.model.RsvpStatus;
import com.example.weddingplanner.model.Wedding;
import com.example.weddingplanner.service.GuestService;
import com.example.weddingplanner.service.WeddingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GuestController {

    private final GuestService guestService;
    private final WeddingService weddingService;

    public GuestController(GuestService guestService, WeddingService weddingService){
        this.guestService = guestService;
        this.weddingService = weddingService;
    }

    @GetMapping("/guests")
    public String showGuests(Model model){
        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()->new IllegalStateException("Wedding has not been created yet."));

        model.addAttribute("guests", guestService.getGuestsForWedding(wedding.getId()));
        model.addAttribute("guest", new Guest());
        model.addAttribute("rsvpStatuses", RsvpStatus.values());

        return "guests";
    }

    @PostMapping("/guests/save")
    public String saveGuest(@ModelAttribute Guest guest){
        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()-> new IllegalStateException("Wedding has not been created yet."));

        guest.setWedding(wedding);
        guestService.saveGuest(guest);

        return "redirect:/guests";
    }

    @GetMapping("/guests/edit/{id}")
    public String editGuest(@PathVariable Long id, Model model){
        Guest guest = guestService.getGuestById(id)
                .orElseThrow(() -> new IllegalArgumentException("Guest not found"));

        model.addAttribute("guest", guest);
        model.addAttribute("rsvpStatuses", RsvpStatus.values());

        return "guest-edit";
    }

    @PostMapping("/guests/update")
    public String updateGuest(@ModelAttribute Guest guest){
        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()-> new IllegalStateException("Wedding has not been created yet"));

        guest.setWedding(wedding);
        guestService.saveGuest(guest);

        return "redirect:/guests";
    }

    @PostMapping("/guests/delete/{id}")
    public String deleteGuest(@PathVariable Long id){
        guestService.deleteGuest(id);

        return "redirect:/guests";
    }

}
