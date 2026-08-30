package com.example.weddingplanner.service;

import com.example.weddingplanner.dto.RegisterRequest;
import com.example.weddingplanner.model.User;

import java.util.Optional;

public interface UserService {

    User registerUser(RegisterRequest registerRequest);

    Optional<User> findByEmail(String email);

    boolean emailExists(String email);
}
