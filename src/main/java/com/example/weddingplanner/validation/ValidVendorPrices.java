package com.example.weddingplanner.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VendorPricesValidator.class)
public @interface ValidVendorPrices {

    String message() default "Deposit amount cannot be greater than estimated price.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
