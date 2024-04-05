package com.woowa.room.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomResponseDto {
    private Long id;
    private String roomName;

    @Builder
    public RoomResponseDto(Long id, String roomName) {
        this.id = id;
        this.roomName = roomName;
    }
}
