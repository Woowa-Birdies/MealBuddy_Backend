package com.woowa.gather.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AskRequest {
    private Long userId;
    private Long postId;

}
