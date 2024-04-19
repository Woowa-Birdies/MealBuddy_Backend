package com.woowa.review.domain.dto;

import com.woowa.user.domain.Gender;
import com.woowa.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UserInfoDto {
    private Long userId;
    private String nickname;
    private String introduce;
    private LocalDateTime birthDate;
    private Gender gender;
    private String email;
    private String image;
    private String position;
    public static UserInfoDto fromUser(User user) {
        return new UserInfoDto(
                user.getId(),
                user.getNickname(),
                user.getIntroduce(),
                user.getBirthDate(),
                user.getGender(),
                user.getEmail(),
                user.getImage(),
                null
        );
    }
}
