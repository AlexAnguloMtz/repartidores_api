package com.aramdev.delivery.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({
        FIELD,
        METHOD,
        PARAMETER,
        ANNOTATION_TYPE
})
@Retention(RUNTIME)
public @interface ValidPassword {

    String message() default "La contraseña no es válida";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}