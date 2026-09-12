package com.aramdev.delivery.controller;

import com.aramdev.delivery.domain.GlobalErrorCodes;
import com.aramdev.delivery.exception.BusinessValidationException;
import com.aramdev.delivery.util.ProblemDetailError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessValidationException.class)
    public ProblemDetail handle(BusinessValidationException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Validacion de negocio");
        problem.setDetail("Validacion de negocio no satisfecha");
        problem.setProperty("error_code", ex.getErrorCode());
        problem.setProperty("errors", List.of());
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handle(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Campos invalidos");
        problem.setProperty("error_code", GlobalErrorCodes.CAMPOS_INVALIDOS.name());

        List<ProblemDetailError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ProblemDetailError(
                        error.getField(),
                        error.getDefaultMessage() == null ? "" : StringUtils.capitalize(error.getDefaultMessage())
                ))
                .toList();

        problem.setProperty("errors", errors);

        return problem;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handle(AuthenticationException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problem.setTitle("Credenciales invalidas");
        problem.setDetail("Las credenciales no son validas");
        problem.setProperty("error_code", GlobalErrorCodes.AUTENTICACION_INVALIDA.name());
        problem.setProperty("errors", List.of());
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handle(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle("Error del servidor");
        problem.setProperty("error_code", GlobalErrorCodes.ERROR_SERVIDOR.name());
        problem.setProperty("errors", List.of());
        return problem;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handle(HttpMessageNotReadableException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Campos invalidos");
        problem.setProperty("error_code", GlobalErrorCodes.CAMPOS_INVALIDOS.name());

        List<ProblemDetailError> errors = new ArrayList<>();

        if (ex.getCause() instanceof InvalidFormatException cause
                && !cause.getPath().isEmpty()) {
            String field = cause.getPath().getLast().getPropertyName();
            String error = "Tipo de dato incorrecto.";
            errors.add(new ProblemDetailError(field, error));
        }

        problem.setProperty("errors", errors);

        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        problem.setTitle("Permisos insuficientes");
        problem.setDetail("No tienes permisos para esta operacion");
        problem.setProperty("error_code", GlobalErrorCodes.PERMISOS_INSUFICIENTES.name());
        problem.setProperty("errors", List.of());
        return problem;
    }

}