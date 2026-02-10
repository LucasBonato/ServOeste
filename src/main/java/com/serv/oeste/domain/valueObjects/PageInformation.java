package com.serv.oeste.domain.valueObjects;

public record PageInformation(
        long totalElements,
        int totalPages,
        int page,
        int size
) { }
