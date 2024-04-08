package com.woowa.gather.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Age {
    AGE20S("20대"),
    AGE30S("30대"),
    AGE40S("40대"),
    AGE50S("50대"),
    NOLIMIT("제한없음")
    ;

    @JsonValue
    private final String value;
}
