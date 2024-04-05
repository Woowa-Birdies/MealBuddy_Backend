package com.woowa.gather.domain.dto;

import com.woowa.gather.domain.enums.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostCreate {
    @NotNull
    private int userId;

    @NotNull
    private String place;
    private Point point;
    private String address;

    @NotNull
    private int participantTotal;
    @NotNull
    private int participantCount;

    private String contents;

    @NotNull
    private PostStatus postStatus;

    private FoodType foodTypeTag;
    private Age ageTag;
    private Gender genderTag;

    @NotNull
    private LocalDateTime meetAt;
    @NotNull
    private LocalDateTime closeAt;

    @Builder
    public PostCreate(int userId, Long locationId, int participantTotal, String contents, FoodType foodTypeTag, Age ageTag, Gender genderTag, LocalDateTime meetAt, LocalDateTime closeAt) {
        this.userId = userId;
        this.locationId = locationId;
        this.participantTotal = participantTotal;
        this.participantCount = 0; // 게시글 생성 시 기본값 0
        this.contents = contents;
        this.postStatus = PostStatus.ONGOING; // 게시글 생성 시 기본값 ONGOING
        this.foodTypeTag = foodTypeTag;
        this.ageTag = ageTag;
        this.genderTag = genderTag;
        this.meetAt = meetAt;
        this.closeAt = closeAt;
    }
}
