package com.woowa.gather.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    MALE("남자"),
    FEMALE("여자"),
    OTHER("기타")
    ;

    @JsonValue
    private final String value;
}
