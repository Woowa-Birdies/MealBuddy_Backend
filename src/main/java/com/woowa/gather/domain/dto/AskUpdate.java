package com.woowa.gather.domain.dto;

import com.woowa.gather.domain.enums.AskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AskUpdate {
    @NotNull
    private Long userId;
    @NotNull
    private Long postId;
    @NotNull
    private Long askId;
    @NotNull
    private AskStatus askStatus;
}
