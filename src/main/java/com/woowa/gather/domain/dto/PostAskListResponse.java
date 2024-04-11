package com.woowa.gather.domain.dto;


import com.woowa.gather.domain.enums.AskStatus;
import com.woowa.user.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostAskListResponse {

    private Long userId;
    private AskStatus askStatus;
    private Gender gender;
    private int age;
    private String introduce;
}
