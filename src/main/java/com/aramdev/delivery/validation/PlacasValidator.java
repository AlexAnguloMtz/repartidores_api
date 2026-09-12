package com.aramdev.delivery.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class PlacasValidator implements ConstraintValidator<ValidPlacas, String> {

    @Override
    public boolean isValid(
            String value,
            ConstraintValidatorContext context
    ) {
        if (!StringUtils.hasText(value)) {
            return true;
        }

        boolean valid = true;

        context.disableDefaultConstraintViolation();

        if (value.length() < 6) {
            addError(context, "Las placas no pueden tener menos de 6 caracteres");
            valid = false;
        }

        if (value.length() > 15) {
            addError(context, "Las placas no pueden tener más de 15 caracteres");
            valid = false;
        }

        if (!value.matches("[A-Za-z0-9-]+")) {
            addError(context, "Las placas solo pueden contener letras, números y guiones");
            valid = false;
        }

        return valid;
    }

    private void addError(
            ConstraintValidatorContext context,
            String message
    ) {
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }

}