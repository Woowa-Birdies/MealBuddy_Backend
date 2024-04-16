package com.woowa.chat.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatResponseDto {
    private String messageId;
    private String message;
    private Long sender;
    private Long roomId;
    private LocalDateTime createAt;

    @Builder
    public ChatResponseDto(String messageId, String message, Long sender, Long roomId, LocalDateTime createAt) {
        this.messageId = messageId;
        this.message = message;
        this.sender = sender;
        this.roomId = roomId;
        this.createAt = createAt;
    }
}
