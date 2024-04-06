package com.woowa.gather.domain.dto;

import com.woowa.gather.domain.enums.Age;
import com.woowa.gather.domain.enums.FoodType;
import com.woowa.gather.domain.enums.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDetails {
    private Long postId;

    private Long userId;
    private String nickname;

    private LocalDateTime meetAt;
    private LocalDateTime closeAt;

    private FoodType foodTypeTag;
    private Age ageTag;
    private Gender genderTag;

    private int participantTotal;
    private int participantCount;

    private String place;
    private String address;

    private String contents;
}
