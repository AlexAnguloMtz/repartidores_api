package com.aramdev.delivery.util;

public record ProblemDetailError(
        String field,
        String error
) {
}