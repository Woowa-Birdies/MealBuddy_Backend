package com.woowa.chat.domain.dto;

import com.woowa.chat.domain.Chat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
/*
{
	"messageId":"0032010",
	"message": "메시지 입니다.",
	"sender": 45,
	"roomId":32,
	"createdAt":"2019-112-20T11:20:33.333Z"
}
 */
public class MessageResponseDto {
    private String messageId;
    private String message;
    private Long sender;
    private Long roomId;
    private LocalDateTime createAt;

    @Builder
    public MessageResponseDto(String messageId, String message, Long sender, Long roomId, LocalDateTime createAt) {
        this.messageId = messageId;
        this.message = message;
        this.sender = sender;
        this.roomId = roomId;
        this.createAt = createAt;
    }

    public MessageResponseDto from(Chat chat) {
        return MessageResponseDto.builder()
                .messageId(chat.getMessageId())
                .message(chat.getMessage())
                .sender(chat.getSender())
                .roomId(chat.getRoomId())
                .createAt(chat.getCreateAt())
                .build();
    }
}
