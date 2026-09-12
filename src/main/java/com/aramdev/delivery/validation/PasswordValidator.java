package com.aramdev.delivery.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 100;

    private static final String SPECIAL_CHARACTERS = "! @ # $ % & * ?";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isEmpty()) {
            return true;
        }

        List<String> errors = new ArrayList<>();

        if (password.length() < MIN_LENGTH) {
            errors.add("La contraseña debe tener al menos 8 caracteres");
        }

        if (password.length() > MAX_LENGTH) {
            errors.add("La contraseña no puede tener más de 100 caracteres");
        }

        if (!password.chars().allMatch(this::isAllowedCharacter)) {
            errors.add(
                    "La contraseña solo puede contener letras, números y caracteres especiales: " + SPECIAL_CHARACTERS
            );
        }

        if (!password.chars().anyMatch(this::isUppercase)) {
            errors.add("La contraseña debe contener al menos una letra mayúscula (A-Z)");
        }

        if (!password.chars().anyMatch(this::isLowercase)) {
            errors.add("La contraseña debe contener al menos una letra minúscula (a-z)");
        }

        if (!password.chars().anyMatch(this::isDigit)) {
            errors.add("La contraseña debe contener al menos un número (0-9)");
        }

        if (!password.chars().anyMatch(this::isSpecialCharacter)) {
            errors.add(
                    "La contraseña debe contener al menos uno de estos caracteres especiales: "
                            + SPECIAL_CHARACTERS
            );
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

    private boolean isAllowedCharacter(int character) {
        return isUppercase(character)
                || isLowercase(character)
                || isDigit(character)
                || isSpecialCharacter(character);
    }

    private boolean isUppercase(int character) {
        return character >= 'A' && character <= 'Z';
    }

    private boolean isLowercase(int character) {
        return character >= 'a' && character <= 'z';
    }

    private boolean isDigit(int character) {
        return character >= '0' && character <= '9';
    }

    private boolean isSpecialCharacter(int character) {
        return character == '!'
                || character == '@'
                || character == '#'
                || character == '$'
                || character == '%'
                || character == '&'
                || character == '*'
                || character == '?';
    }
}