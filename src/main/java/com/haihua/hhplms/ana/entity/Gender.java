package com.haihua.hhplms.ana.entity;

import com.haihua.hhplms.common.entity.BaseEnum;

public enum Gender implements BaseEnum {
    MALE("male", "男"),
    FEMALE("female", "女"),
    UNKNOWN("unknown", "未知");

    private String code;
    private String name;

    Gender(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
