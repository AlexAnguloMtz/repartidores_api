package com.aramdev.delivery.controller;

import com.aramdev.delivery.domain.GlobalErrorCodes;
import com.aramdev.delivery.exception.BusinessValidationException;
import com.aramdev.delivery.util.ProblemDetailError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessValidationException.class)
    public ProblemDetail handle(BusinessValidationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setTitle("Validacion de negocio");
        problemDetail.setDetail("Validacion de negocio no satisfecha");
        problemDetail.setProperty("error_code", ex.getErrorCode());
        problemDetail.setProperty("errors", List.of());
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handle(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Datos inválidos");
        problemDetail.setProperty("error_code", GlobalErrorCodes.CAMPOS_INVALIDOS.name());

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
        problemDetail.setDetail("Las credenciales no son válidas");
        problemDetail.setProperty("error_code", GlobalErrorCodes.AUTENTICACION_INVALIDA.name());
        problemDetail.setProperty("errors", List.of());
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handle(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Error del servidor");
        problemDetail.setProperty("error_code", GlobalErrorCodes.ERROR_SERVIDOR.name());
        problemDetail.setProperty("errors", List.of());
        return problemDetail;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        problemDetail.setTitle("Acceso denegado");
        problemDetail.setDetail("No tienes permisos para esta operación");
        problemDetail.setProperty("error_code", GlobalErrorCodes.PERMISOS_INSUFICIENTES.name());
        return problemDetail;
    }

}