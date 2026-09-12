package com.aramdev.delivery.util;

import java.util.List;

public record OffsetPaginationResponse<T>(
        List<T> items,
        int pageNumber,
        int pageSize,
        long totalItems,
        int totalPages,
        boolean hasPreviousPage,
        boolean hasNextPage
) {
}