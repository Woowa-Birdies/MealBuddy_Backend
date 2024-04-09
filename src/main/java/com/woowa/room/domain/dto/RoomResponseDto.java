package com.woowa.room.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomResponseDto {
    private Long roomId;
    private String roomName;

    @Builder
    public RoomResponseDto(Long roomId, String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }
}
