package com.woowa.chat.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class StatusMessageDto {
    private Long roomId;
    private LocalDateTime lastReadAt;

    @Builder
    public StatusMessageDto(Long roomId, LocalDateTime lastReadAt) {
        this.roomId = roomId;
        this.lastReadAt = lastReadAt;
    }
}
