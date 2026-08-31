package com.example.weddingplanner.service;

import com.example.weddingplanner.model.Guest;
import com.example.weddingplanner.model.RsvpStatus;

import java.util.List;
import java.util.Optional;

public interface GuestService {

    List<Guest> getGuestsForWedding(Long weddingId);

    Optional<Guest> getGuestById(Long id, Long weddingId);

    Guest saveGuest(Guest guest);

    void deleteGuest (Long id, Long weddingId);

    long countGuests(Long weddingId);

    long countConfirmedGuests(Long weddingId);

    long countDeclinedGuests(Long weddingId);

    long countInvitedGuests(Long weddingId);

    List<Guest> getGuestsByStatus(Long weddingId, RsvpStatus status);

    List<Guest> searchGuests(Long weddingId, String search);

    List<Guest> searchGuestsByStatus(Long weddingId, String search, RsvpStatus status);

}
