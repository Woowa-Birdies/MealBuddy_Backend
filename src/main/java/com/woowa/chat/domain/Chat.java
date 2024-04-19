package com.woowa.chat.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Document(collection = "chat")
public class Chat {
    @Id
    @Field(value = "_id", targetType = FieldType.OBJECT_ID)
    private String messageId;

    private Long roomId;

    private String message;

    private Long sender;

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    @Builder
    public Chat(String messageId, Long roomId, String message, Long sender) {
        this.messageId = messageId;
        this.roomId = roomId;
        this.message = message;
        this.sender = sender;
    }
}
