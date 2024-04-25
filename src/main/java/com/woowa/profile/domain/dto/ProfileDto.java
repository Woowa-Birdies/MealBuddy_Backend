package com.woowa.profile.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.woowa.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class ProfileDto {
    private Long userId;
    private String nickname;        //닉네임
    private String introduce;       //소개글

    private int goodPunctuality;    // 약속(매너)
    private int badPunctuality;    // 약속(비매너)

    private int goodSociability;    // 사교성(매너)
    private int badSociability;    // 사교성(비매너)

    private int goodManner;         // 식사 매너(매너)
    private int badManner;         // 식사 매너(비매너)

    private int goodReply;          // 응답 속도(매너)
    private int badReply;          // 응답 속도(비매너)

    private List<ParticipationDto> participationInfos;
    private List<RecruitmentDto> recruitmentInfos;
    public static ProfileDto fromUser(
            User user,
            int goodPunctuality,int badPunctuality,int goodSociability,int badSociability,
            int goodManner, int badManner, int goodReply, int badReply,
            List<ParticipationDto> participationInfos, List<RecruitmentDto> recruitmentInfos) {
        return new ProfileDto(user.getId(),user.getNickname(),user.getIntroduce(),
                goodPunctuality,badPunctuality,goodSociability,badSociability,goodManner,badManner,goodReply,badReply,
                participationInfos,recruitmentInfos);
    }
}
