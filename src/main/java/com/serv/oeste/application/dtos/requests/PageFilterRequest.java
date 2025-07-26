package com.serv.oeste.application.dtos.requests;

import com.serv.oeste.domain.valueObjects.PageFilter;

public record PageFilterRequest(
    Integer size,
    Integer page
) {
    public PageFilter toPageFilter() {
        int finalSize = size != null ? size : 10;
        int finalPage = page != null ? page : 0;

        return new PageFilter(
            finalSize,
            finalPage
        );
    }
}
