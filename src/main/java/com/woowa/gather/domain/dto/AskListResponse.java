package com.woowa.gather.domain.dto;

import com.woowa.gather.domain.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AskListResponse {

    private Long askId;
    private Long postId;
    private Long userId;
    private FoodType foodType;
    private Gender genderTag;
    private Age ageTag;
    private String address;
    private String place;
    private int participantTotal;
    private int participantCount;
    private PostStatus postStatus;
    private AskStatus askStatus;
    private LocalDateTime meetAt;
    private LocalDateTime closeAt;
    private LocalDateTime createdAt;

}
