package com.example.weddingplanner.service;

import com.example.weddingplanner.model.Guest;
import com.example.weddingplanner.model.RsvpStatus;
import com.example.weddingplanner.repository.GuestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuestServiceImpl implements GuestService{

    private final GuestRepository guestRepository;

    public GuestServiceImpl(GuestRepository guestRepository){
        this.guestRepository = guestRepository;
    }

    @Override
    public List<Guest> getGuestsForWedding(Long weddingId){
        return guestRepository.findByWeddingId(weddingId);
    }

    @Override
    public Optional<Guest> getGuestById(Long id, Long weddingId){
        return guestRepository.findByIdAndWeddingId(id, weddingId);
    }

    @Override
    public Guest saveGuest(Guest guest){

        if(!guest.isPlusOne()){
            guest.setPlusOneFirstName(null);
            guest.setPlusOneLastName(null);
        }
        return guestRepository.save(guest);
    }

    @Override
    public void deleteGuest(Long id, Long weddingId){
        guestRepository.findByIdAndWeddingId(id, weddingId).ifPresent(guestRepository::delete);
    }

    @Override
    public long countGuests(Long weddingId){

        return guestRepository.findByWeddingId(weddingId)
                .stream()
                .mapToLong(guest -> guest.isPlusOne() ? 2 : 1)
                .sum();
    }

    @Override
    public long countConfirmedGuests(Long weddingId){
        return guestRepository.findByWeddingId(weddingId)
                .stream()
                .filter(guest -> guest.getRsvpStatus() == RsvpStatus.CONFIRMED)
                .mapToLong(guest -> guest.isPlusOne() ? 2 : 1)
                .sum();
    }

    @Override
    public long countDeclinedGuests(Long weddingId){
        return guestRepository.findByWeddingId(weddingId)
                .stream()
                .filter(guest -> guest.getRsvpStatus() == RsvpStatus.DECLINED)
                .mapToLong(guest -> guest.isPlusOne() ? 2 : 1)
                .sum();
    }

    @Override
    public long countInvitedGuests(Long weddingId){
        return guestRepository.findByWeddingId(weddingId)
                .stream()
                .filter(guest -> guest.getRsvpStatus() == RsvpStatus.INVITED)
                .mapToLong(guest -> guest.isPlusOne() ? 2 : 1)
                .sum();
    }

}
