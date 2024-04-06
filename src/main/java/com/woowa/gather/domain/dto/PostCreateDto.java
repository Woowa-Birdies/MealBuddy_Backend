package com.woowa.gather.domain.dto;

import com.woowa.gather.domain.enums.*;
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
//    @NotNull
//    private int userId;

    @NotBlank
    private String place;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
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
    private PostStatus postStatus = PostStatus.ONGOING; // 게시글 생성 시 ONGOING

    private FoodType foodTypeTag;
    private Age ageTag;
    private Gender genderTag;

    @NotNull
    private LocalDateTime meetAt;
    @NotNull
    private LocalDateTime closeAt;
}
