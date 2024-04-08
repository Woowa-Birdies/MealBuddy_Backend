package com.woowa.gather.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AskResponse {
    private Long postId;
    private Long askUserId;
    private int participantCount;
    private int participantTotal;
}
