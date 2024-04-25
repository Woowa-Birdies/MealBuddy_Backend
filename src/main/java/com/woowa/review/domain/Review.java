package com.woowa.review.domain;

import com.woowa.common.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;
    @NotNull
    @Column(name = "POST_ID")
    private Long postId;           //게시물 아이디

    @Column(name = "USER_ID")
    private Long userId;           //유저 아이디

    @Column(name = "PARTICIPATION")
    private Boolean participation;  //참여 여부

    @Column(name = "PUNCTUALITY")
    private Boolean punctuality;    // 약속

    @Column(name = "SOCIABILITY")
    private Boolean sociability;    // 사교성

    @Column(name = "MANNER")
    private Boolean manner;         // 식사 매너

    @Column(name = "REPLY")
    private Boolean reply;          // 응답 속도
}
