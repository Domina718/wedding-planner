package com.example.weddingplanner.controller;

import com.example.weddingplanner.dto.RegisterRequest;
import com.example.weddingplanner.model.User;
import com.example.weddingplanner.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService){

        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerPage(Model model){

        model.addAttribute("registerRequest", new RegisterRequest());

        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("registerRequest") RegisterRequest registerRequest,
            BindingResult bindingResult,
            Model model){

        if(!registerRequest.getPassword()
                .equals(registerRequest.getConfirmPassword())){
            bindingResult.rejectValue(
                    "confirmPassword",
                    "password.mismatch",
                    "Passwords do not match."
            );
        }

        if(userService.emailExists(registerRequest.getEmail())){

            bindingResult.rejectValue(
                    "email",
                    "email.exists",
                    "Email is already registered."
            );
        }

        if (bindingResult.hasErrors()){

            return "register";
        }

        userService.registerUser(registerRequest);

        return "redirect:/login?registered";
    }

    @GetMapping("/login")
    public String loginPage(){
        return"login";
    }
}
