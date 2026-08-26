package com.example.weddingplanner.service;

import com.example.weddingplanner.model.Wedding;
import com.example.weddingplanner.repository.WeddingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WeddingServiceImpl implements WeddingService {

    private final WeddingRepository weddingRepository;

    public WeddingServiceImpl(WeddingRepository weddingRepository){
        this.weddingRepository = weddingRepository;
    }

    @Override
    public Optional<Wedding> getWedding(){
        return weddingRepository.findAll().stream().findFirst();
    }

    @Override
    public Wedding saveWedding(Wedding wedding){
        Optional<Wedding> existingWedding = getWedding();

        if(existingWedding.isPresent()){
            wedding.setId(existingWedding.get().getId());
        }

        return weddingRepository.save(wedding);
    }

    @Override
    public void deleteWedding(){
        getWedding().ifPresent(weddingRepository::delete);
    }
}
