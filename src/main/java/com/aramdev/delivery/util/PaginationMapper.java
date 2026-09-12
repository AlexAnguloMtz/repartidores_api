package com.aramdev.delivery.util;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PaginationMapper {

    public <U, T> OffsetPaginationResponse<U> toOffsetPaginationResponse(
            Page<T> page,
            Function<T, U> mapper
    ) {
        return new OffsetPaginationResponse<>(
                page.getContent().stream()
                        .map(mapper)
                        .toList(),
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasPrevious(),
                page.hasNext()
        );
    }

}
