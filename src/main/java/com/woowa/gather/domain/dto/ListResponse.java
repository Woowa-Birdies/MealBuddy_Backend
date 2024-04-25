package com.woowa.gather.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ListResponse<T> {
    private List<T> result;
    private PageInfo pageInfo;

    public ListResponse(List<T> result, int page, int size, long totalElements, int totalPages) {
        this.result = result;
        this.pageInfo = new PageInfo(page, size, totalElements, totalPages);
    }

    @Getter
    @Builder
    private static class PageInfo {
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
    }
}
