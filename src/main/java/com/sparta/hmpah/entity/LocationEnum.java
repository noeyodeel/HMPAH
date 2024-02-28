package com.sparta.hmpah.entity;

import java.util.Arrays;

public enum LocationEnum {
    HONGDAE("HONGDAE"), GANGNAM("GANGNAM"), ITAEWON("ITAEWON");
    private final String label;

    LocationEnum(String label){
        this.label = label;
    }

    public String getLabel(){
        return label;
    }

    public static LocationEnum getEnum(String label){
        return Arrays.stream(values())
            .filter(value -> value.label.equals(label))
            .findAny()
            .orElse(null);
    }
}
