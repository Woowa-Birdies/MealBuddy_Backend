package com.woowa.chat.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class StatusResponseDto {
    private Long roomId;
    private List<StatusMessageDto> joinUsers;

    @Builder
    public StatusResponseDto(Long roomId, List<StatusMessageDto> joinUsers) {
        this.roomId = roomId;
        this.joinUsers = joinUsers;
    }
}
