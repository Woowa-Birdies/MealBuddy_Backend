package com.woowa.gather.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AskStatus {
    WAITING("대기"),
    ACCEPTED("수락"),
    REJECTED("거절"),
    PARTICIPATION("참여"),
    NON_PARTICIPATION("미참여"),
    ;

    @JsonValue
    private final String value;
}
