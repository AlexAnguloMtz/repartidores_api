package com.aramdev.delivery.util;

import java.util.Set;

public record DeletionSummaryResponse<T>(
        Set<T> deletedIds,
        Set<T> notDeletedIds
) {
}