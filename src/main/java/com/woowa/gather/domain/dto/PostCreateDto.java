package com.woowa.gather.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.woowa.gather.domain.enums.*;
import com.woowa.gather.serializer.KSTLocalDateTimeDeserializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateDto {
    @NotNull
    private Long userId;

    @NotBlank
    private String place;
    @NotBlank
    private String address;

    @NotNull
    private int participantTotal;

    @NotNull
    @Builder.Default
    private int participantCount = 0; // 게시글 생성 시 기본값 0

    private String contents;

    @NotNull
    @Builder.Default
    private PostStatus postStatus = PostStatus.ONGOING; // 게시글 생성 시 기본값 ONGOING

    @NotNull
    private FoodType foodTypeTag;
    @NotNull
    private Age ageTag;
    @NotNull
    private Gender genderTag;

    @NotNull
    @JsonDeserialize(using = KSTLocalDateTimeDeserializer.class)
    private LocalDateTime meetAt;
    @NotNull
    @JsonDeserialize(using = KSTLocalDateTimeDeserializer.class)
    private LocalDateTime closeAt;
}
