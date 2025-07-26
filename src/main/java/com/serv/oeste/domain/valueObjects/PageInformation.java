package com.serv.oeste.domain.valueObjects;

public class PageInformation {
    private final long totalElements;
    private final int totalPages;
    private final int page;
    private final int size;

    PageInformation(long totalElements, int totalPages, int page, int size) {
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.page = page;
        this.size = size;
    }

    public long getTotalElements() { return totalElements; }
    public int getTotalPages() { return totalPages; }
    public int getPage() { return page; }
    public int getSize() { return size; }
}
