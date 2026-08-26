package com.example.weddingplanner.service;

import com.example.weddingplanner.model.Wedding;

import java.util.Optional;

public interface WeddingService {

    Optional<Wedding> getWedding();

    Wedding saveWedding(Wedding wedding);

    void deleteWedding();
}
