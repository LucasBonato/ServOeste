package com.serv.oeste.domain.valueObjects;

import java.util.List;
import java.util.function.Function;

public class PageResponse<T> {
    private final List<T> content;
    private final PageInformation page;

    public PageResponse(List<T> content, int totalPages, int page, int size) {
        this.content = content;
        this.page = new PageInformation(
                content.size(),
                totalPages,
                page,
                size
        );
    }

    private PageResponse(List<T> content, PageInformation page) {
        this.content = content;
        this.page = page;
    }

    public List<T> getContent() { return content; }
    public PageInformation getPage() { return page; }


    public <U> PageResponse<U> map(Function<? super T, U> converter) {
        List<U> mappedContent = content.stream()
                .map(converter)
                .toList();

        return new PageResponse<>(
                mappedContent,
                page
        );
    }
}

