package com.woowa.gather.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FoodType {
    CAFE("커피"),
    MEAL("밥"),
    ALCOHOL("술"),
    DESSERT("간식")
    ;

    @JsonValue
    private final String value;
}
