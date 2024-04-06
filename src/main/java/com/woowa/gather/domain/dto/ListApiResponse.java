package com.woowa.gather.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ListApiResponse<T> {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public int resultCount;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<T> ongoing;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<T> completion;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<T> closed;
}
