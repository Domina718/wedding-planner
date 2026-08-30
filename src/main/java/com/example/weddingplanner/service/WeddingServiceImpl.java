package com.example.weddingplanner.service;

import com.example.weddingplanner.model.User;
import com.example.weddingplanner.model.Wedding;
import com.example.weddingplanner.repository.UserRepository;
import com.example.weddingplanner.repository.WeddingRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WeddingServiceImpl implements WeddingService {

    private final WeddingRepository weddingRepository;
    private final UserRepository userRepository;

    public WeddingServiceImpl(WeddingRepository weddingRepository,
                              UserRepository userRepository){
        this.weddingRepository = weddingRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(){

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalStateException("Logged-in user not found."));
    }

    @Override
    public Optional<Wedding> getWedding(){

        User currentUser = getCurrentUser();

        return weddingRepository.findByUser(currentUser);
    }

    @Override
    public Wedding saveWedding(Wedding wedding){

        User currentUser = getCurrentUser();

        Optional<Wedding> existingWedding = weddingRepository.findByUser(currentUser);

        if(existingWedding.isPresent()){
            wedding.setId(existingWedding.get().getId());
        }

        wedding.setUser(currentUser);

        return weddingRepository.save(wedding);
    }

    @Override
    public void deleteWedding(){
        getWedding().ifPresent(weddingRepository::delete);
    }
}
