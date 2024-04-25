package com.woowa.gather.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListApiResponse<T> {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public int resultCount;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public PageInfo pageInfo;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<T> ongoing;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<T> completion;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<T> closed;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<T> waitingOrRejected;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<T> accepted;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<T> participated;

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public List<T> result;

    public ListApiResponse (int listType, int type, List<T> list, int page, int size, long totalElements, int totalPages) {
        if (listType == 0) {
            switch (type) {
                case 0 -> this.ongoing = list;
                case 1 -> this.completion = list;
                case 2 -> this.closed = list;
            }
        } else if (listType == 1) {
            switch (type) {
                case 0 -> this.waitingOrRejected = list;
                case 1 -> this.accepted = list;
                case 2 -> this.participated = list;
            }
        }

        this.pageInfo = PageInfo.builder()
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build();

        this.resultCount = list.size();
    }

    @Getter @Setter @Builder
    private static class PageInfo{
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
    }
}
