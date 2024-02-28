package com.sparta.hmpah.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

public enum UserGenderEnum {
    MALE("MALE"),
    FEMALE("FEMALE");

    @Getter
    private final String value;
    UserGenderEnum(String value) {
        this.value = value;
    }



}
