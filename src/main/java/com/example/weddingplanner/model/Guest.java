package com.example.weddingplanner.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "guests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    private String email;
    private String phone;

    @Enumerated(EnumType.STRING)
    private RsvpStatus rsvpStatus;

    private boolean plusOne;
    private String plusOneFirstName;
    private String plusOneLastName;

    @ManyToOne
    @JoinColumn(name = "wedding_id")
    private Wedding wedding;
}
