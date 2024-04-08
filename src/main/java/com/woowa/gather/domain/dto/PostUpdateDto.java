package com.woowa.gather.domain.dto;

import com.woowa.gather.domain.enums.Age;
import com.woowa.gather.domain.enums.FoodType;
import com.woowa.gather.domain.enums.Gender;
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
public class PostUpdateDto {
    @NotNull
    private Long postId;

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

    private String contents;

    @NotNull
    private FoodType foodTypeTag;
    @NotNull
    private Age ageTag;
    @NotNull
    private Gender genderTag;

    @NotNull
    private LocalDateTime meetAt;
    @NotNull
    private LocalDateTime closeAt;
}
