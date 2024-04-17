package com.woowa.profile.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileDto {
    private Long reviewId;
    private Long userId;

    public ProfileDto(Long reviewId, Long userId) {
        this.reviewId = reviewId;
        this.userId = userId;
    }
}
