package com.aramdev.delivery.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PlacasValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPlacas {

    String message() default "Placas inválidas";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}