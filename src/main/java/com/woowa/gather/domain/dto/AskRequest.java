package com.woowa.gather.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class AskRequest {
    private Long userId;
    private Long postId;

}
