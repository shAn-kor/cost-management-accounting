package com.costflow.accounting.interfaces.api.common;

import java.util.List;

public record PageResponse<T>(
    List<T> items,
    int page,
    int size,
    long totalElements,
    int totalPages
) {
}
