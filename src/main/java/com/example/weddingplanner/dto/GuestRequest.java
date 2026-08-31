package com.example.weddingplanner.dto;

import com.example.weddingplanner.model.RsvpStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GuestRequest {

    private Long id;

    @NotBlank(message = "First name is required.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    @Email(message = "Enter a valid email address.")
    private String email;

    private String phone;

    private RsvpStatus rsvpStatus;

    private boolean plusOne;

    private String plusOneFirstName;

    private String plusOneLastName;
}
