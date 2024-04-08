package com.woowa.gather.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class AskRequest {
    @NotNull
    private Long userId;
    @NotNull
    private Long postId;

}
