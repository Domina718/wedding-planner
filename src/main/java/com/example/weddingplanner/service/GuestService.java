package com.example.weddingplanner.service;

import com.example.weddingplanner.model.Guest;

import java.util.List;
import java.util.Optional;

public interface GuestService {

    List<Guest> getGuestsForWedding(Long weddingId);

    Optional<Guest> getGuestById(Long id);

    Guest saveGuest(Guest guest);

    void deleteGuest (Long id);

    long countGuests(Long weddingId);

    long countConfirmedGuests(Long weddingId);

    long countDeclinedGuests(Long weddingId);

    long countPendingGuests(Long weddingId);

}
