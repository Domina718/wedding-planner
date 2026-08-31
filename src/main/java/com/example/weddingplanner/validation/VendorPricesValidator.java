package com.example.weddingplanner.validation;

import com.example.weddingplanner.dto.VendorRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class VendorPricesValidator implements ConstraintValidator<ValidVendorPrices, VendorRequest> {

    @Override
    public boolean isValid(
            VendorRequest vendorRequest,
            ConstraintValidatorContext context){

        if(vendorRequest == null){
            return true;
        }

        if(vendorRequest.getEstimatedPrice() == null || vendorRequest.getDepositAmount() == null){
            return true;
        }

        if(vendorRequest.getDepositAmount().compareTo(vendorRequest.getEstimatedPrice()) > 0){

            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate(
                    "Deposit amount cannot be greater than estimated price.")
                    .addPropertyNode("depositAmount")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}
