package com.aramdev.delivery.util;

import com.aramdev.delivery.domain.GlobalErrorCodes;
import com.aramdev.delivery.exception.BusinessValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

@Component
public class PaginationUtils {

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

    public Sort parseSortOrThrow(String sort, Map<String, String> sorts) {
        int separator = sort.lastIndexOf('-');

        String property = sort.substring(0, separator);
        String direction = sort.substring(separator + 1);

        String mappedProperty = sorts.get(property);

        if (mappedProperty == null) {
            throw new BusinessValidationException(GlobalErrorCodes.SORT_INVALIDO);
        }

        return Sort.by(
                Sort.Direction.fromString(direction),
                mappedProperty
        );
    }

}
