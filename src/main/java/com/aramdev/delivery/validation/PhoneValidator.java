package com.aramdev.delivery.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    private static final int PHONE_LENGTH = 10;

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null || phone.isEmpty()) {
            return true;
        }

        List<String> errors = new ArrayList<>();

        if (phone.length() != PHONE_LENGTH) {
            errors.add("El teléfono debe tener exactamente 10 dígitos");
        }

        if (!phone.chars().allMatch(this::isDigit)) {
            errors.add("El teléfono solo puede contener dígitos");
        }

        if (errors.isEmpty()) {
            return true;
        }

        context.disableDefaultConstraintViolation();

        for (String error : errors) {
            context.buildConstraintViolationWithTemplate(error)
                    .addConstraintViolation();
        }

        return false;
    }

    private boolean isDigit(int character) {
        return character >= '0' && character <= '9';
    }
}