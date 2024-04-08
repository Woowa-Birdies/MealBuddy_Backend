package com.woowa.gather.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    MALE("남자만"),
    FEMALE("여자만"),
    ANYONE("남녀무관")
    ;

    @JsonValue
    private final String value;
}
