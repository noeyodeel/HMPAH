package com.sparta.hmpah.entity;

import java.util.Arrays;

public enum PostStatusEnum {
    RECRUTING("RECRUTING"), COMPLETED("COMPLETED");
    private final String label;
    PostStatusEnum(String label){
        this.label = label;
    }
    public String getLabel(){
        return label;
    }
    public PostStatusEnum getEnum(String label){
        return Arrays.stream(values())
            .filter(value -> value.label.equals(label))
            .findAny()
            .orElse(null);
    }
}
