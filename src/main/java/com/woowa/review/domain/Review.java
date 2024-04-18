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
    private Long postId;           //게시물 아이디

    private Long userId;           //유저 아이디
    private Boolean participation;  //참여 여부
    private Boolean punctuality;    // 약속
    private Boolean sociability;    // 사교성
    private Boolean manner;         // 식사 매너
    private Boolean reply;          // 응답 속도
}
