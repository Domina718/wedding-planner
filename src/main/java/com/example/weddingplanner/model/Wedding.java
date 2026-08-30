package com.example.weddingplanner.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "weddings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wedding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String partnerOneName;

    private String partnerTwoName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate weddingDate;

    private BigDecimal budget;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

}
