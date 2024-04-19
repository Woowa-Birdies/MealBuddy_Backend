package com.woowa.gather.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FoodType {
    MEAL("식사"),
    CAFE("카페"),
    ALCOHOL("술")
    ;

    @JsonValue
    private final String value;
}
