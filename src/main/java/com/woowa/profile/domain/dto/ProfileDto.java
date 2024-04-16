package com.woowa.profile.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileDto {
    private Long review_Id;
    private Long user_Id;

    public ProfileDto(Long review_Id, Long user_Id) {
        this.review_Id = review_Id;
        this.user_Id = user_Id;
    }
}
