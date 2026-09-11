package com.aramdev.delivery.controller;

import com.aramdev.delivery.util.ProblemDetailError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handle(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Datos inválidos");
        problemDetail.setProperty("error_code", "INVALID_FIELDS");

        List<ProblemDetailError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ProblemDetailError(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handle(AuthenticationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setTitle("Credenciales inválidas");
        problemDetail.setProperty("error_code", "BAD_CREDENTIALS");
        problemDetail.setProperty("errors", List.of());
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handle(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Error del servidor");
        problemDetail.setProperty("error_code", "SERVER_ERROR");
        problemDetail.setProperty("errors", List.of());
        return problemDetail;
    }

}