package com.woowa.chat.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor
@Document(collection = "userStatus")
public class UserStatus {

    @Id
    @Field(value = "_id", targetType = FieldType.OBJECT_ID)
    private String StatusId;

    private Long userId;

    private Long roomId;

    private LocalDateTime lastReadAt;

    @Builder
    public UserStatus(String StatusId, Long userId, Long roomId, LocalDateTime lastReadAt) {
        this.StatusId = StatusId;
        this.userId = userId;
        this.roomId = roomId;
        this.lastReadAt = lastReadAt;
    }
}
