package com.example.weddingplanner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class WeddingRequest {

    @NotBlank(message = "Partner one name is required.")
    private String partnerOneName;

    @NotBlank(message = "Partner two name is required.")
    private String partnerTwoName;

    @NotNull(message = "Wedding date is required.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate weddingDate;

    @NotNull(message = "Budget is required.")
    @Positive(message = "Budget must be greater than 0.")
    private BigDecimal budget;
}
