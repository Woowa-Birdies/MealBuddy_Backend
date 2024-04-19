package com.woowa.gather.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.woowa.gather.domain.enums.*;
import com.woowa.gather.serializer.KSTLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailsResponseDto {
    private Long postId;

    private Long userId;
    private String nickname;

    @JsonSerialize(using = KSTLocalDateTimeSerializer.class)
    private LocalDateTime meetAt;
    @JsonSerialize(using = KSTLocalDateTimeSerializer.class)
    private LocalDateTime closeAt;

    private FoodType foodTypeTag;
    private Age ageTag;
    private Gender genderTag;

    private int participantTotal;
    private int participantCount;
    private PostStatus postStatus;

    private String place;
    private String address;

    private String contents;

    @Builder.Default
    private AskStatus askStatus = null;
}
