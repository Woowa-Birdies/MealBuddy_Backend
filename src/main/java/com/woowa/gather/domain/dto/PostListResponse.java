package com.woowa.gather.domain.dto;

import com.woowa.gather.domain.enums.Age;
import com.woowa.gather.domain.enums.FoodType;
import com.woowa.gather.domain.enums.Gender;
import com.woowa.gather.domain.enums.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PostListResponse {

    private Long postId;
    private Long userId;
    private FoodType foodTypeTag;
    private Gender genderTag;
    private Age ageTag;
    private String address;
    private String place;
    private int participantTotal;
    private int participantCount;
    private PostStatus postStatus;
    private LocalDateTime meetAt;
    private LocalDateTime closeAt;
    private LocalDateTime createdAt;

}
