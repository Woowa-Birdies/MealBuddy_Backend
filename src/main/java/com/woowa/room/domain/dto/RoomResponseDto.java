package com.woowa.room.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomResponseDto {
    private Long roomId;
    private String roomName;
    private Long postId;
    private Long userCount;

    @Builder
    public RoomResponseDto(Long roomId, String roomName, Long postId, Long userCount) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.postId = postId;
        this.userCount = userCount;
    }
}
