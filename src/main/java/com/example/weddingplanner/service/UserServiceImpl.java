package com.example.weddingplanner.service;

import com.example.weddingplanner.dto.RegisterRequest;
import com.example.weddingplanner.model.User;
import com.example.weddingplanner.model.Wedding;
import com.example.weddingplanner.repository.UserRepository;
import com.example.weddingplanner.repository.WeddingRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final WeddingRepository weddingRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           WeddingRepository weddingRepository,
                           PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.weddingRepository = weddingRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(RegisterRequest registerRequest){

        if (userRepository.existsByEmail(registerRequest.getEmail())){
           throw new IllegalArgumentException("Email is already registered.");
        }

        User user = new User();

        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole("ROLE_USER");

        User savedUser = userRepository.save(user);

        Wedding wedding = new Wedding();
        wedding.setUser(savedUser);

        weddingRepository.save(wedding);

        return savedUser;
    }

    @Override
    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean emailExists(String email){
        return userRepository.existsByEmail(email);
    }
}
