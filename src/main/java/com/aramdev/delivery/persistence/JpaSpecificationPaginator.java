package com.aramdev.delivery.persistence;

import com.aramdev.delivery.domain.GlobalErrorCodes;
import com.aramdev.delivery.exception.BusinessValidationException;
import com.aramdev.delivery.util.OffsetPaginationRequest;
import com.aramdev.delivery.util.OffsetPaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JpaSpecificationPaginator {

    public <T, F, R> OffsetPaginationResponse<R> findPage(
            OffsetPaginationRequest pagination,
            Specification<T> specification,
            JpaSpecificationExecutor<T> repository,
            Function<T, R> mapper,
            Map<String, String> sorts,
            String defaultSort
    ) {
        PageRequest pageRequest = PageRequest.of(
                pagination.pageNumberOrDefault() - 1,
                pagination.pageSizeOrDefault(),
                parseSort(pagination.sort(), sorts, defaultSort)
        );

        Page<T> page = repository.findAll(
                specification,
                pageRequest
        );

        return toOffsetPaginationResponse(
                page,
                mapper
        );
    }

    private Sort parseSort(
            String sort,
            Map<String, String> sorts,
            String defaultSort
    ) {
        if (!StringUtils.hasText(sort)) {
            return Sort.by(Sort.Direction.ASC, defaultSort);
        }

        return parseSortOrThrow(sort, sorts);
    }

    private <U, T> OffsetPaginationResponse<U> toOffsetPaginationResponse(
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

    private Sort parseSortOrThrow(String sort, Map<String, String> sorts) {
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