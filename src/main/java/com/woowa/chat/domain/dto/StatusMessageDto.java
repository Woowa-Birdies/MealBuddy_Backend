package com.woowa.chat.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class StatusMessageDto {
    private Long userId;
    private LocalDateTime lastReadAt;

    @Builder
    public StatusMessageDto(Long userId, LocalDateTime lastReadAt) {
        this.userId = userId;
        this.lastReadAt = lastReadAt;
    }
}
