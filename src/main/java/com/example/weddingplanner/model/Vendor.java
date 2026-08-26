package com.example.weddingplanner.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "vendors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private VendorServiceType serviceType;

    private String email;

    private String phone;

    private BigDecimal estimatedPrice;

    private BigDecimal depositAmount;

    @Transient
    public BigDecimal getRemainingAmount(){
        if (estimatedPrice == null){
            return BigDecimal.ZERO;
        }

        if(depositAmount == null){
            return estimatedPrice;
        }

        return estimatedPrice.subtract(depositAmount);
    }

    @Enumerated(EnumType.STRING)
    private VendorStatus status;

    @ManyToOne
    @JoinColumn(name = "wedding_id")
    private Wedding wedding;
}
