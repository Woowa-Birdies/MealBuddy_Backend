package com.woowa.gather.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostStatus {
    ONGOING("모집중"),
    COMPLETION("모집 완료"),
    CLOSED("모임 종료"),
    ;

    @JsonValue
    private final String value;
}
