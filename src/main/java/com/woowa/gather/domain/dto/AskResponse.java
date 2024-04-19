package com.woowa.gather.domain.dto;

import com.woowa.gather.domain.enums.AskStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AskResponse {
    private Long askUserId;
    private Long askId;
    private Long postId;
    private AskStatus askStatus;
}
