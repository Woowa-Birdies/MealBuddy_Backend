package com.woowa.room.domain.dto;

import lombok.AllArgsConstructor;

public record JoinRequestDto(long postId) {
    public JoinRequestDto(long postId) {
        this.postId = postId;
    }
}
