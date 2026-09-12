package com.aramdev.delivery.util;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public record OffsetPaginationRequest(

        @Min(1)
        Integer pageNumber,

        @Min(1)
        @Max(100)
        Integer pageSize,

        @Pattern(regexp = "^[a-zA-Z0-9.]+-(asc|desc)$")
        String sort

) {
    public int pageNumberOrDefault() {
        return pageNumber != null ? pageNumber : 1;
    }

    public int pageSizeOrDefault() {
        return pageSize != null ? pageSize : 20;
    }
}