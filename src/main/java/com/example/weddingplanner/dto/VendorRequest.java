package com.example.weddingplanner.dto;

import com.example.weddingplanner.model.VendorServiceType;
import com.example.weddingplanner.model.VendorStatus;
import com.example.weddingplanner.validation.ValidVendorPrices;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@ValidVendorPrices
@Setter
@Getter
@NoArgsConstructor
public class VendorRequest {

    private Long id;

    @NotBlank(message = "Name is required.")
    private String name;

    @NotNull(message = "Service type is required.")
    private VendorServiceType serviceType;

    @Email(message = "Enter a valid email address.")
    private String email;

    private String phone;

    @PositiveOrZero(message = "Estimated price cannot be negative.")
    private BigDecimal estimatedPrice;

    @PositiveOrZero(message = "Deposit amount cannot be negative")
    private BigDecimal depositAmount;

    private VendorStatus status;
}
