package com.haihua.hhplms.ana.entity;

import com.haihua.hhplms.common.entity.BaseEnum;

import java.util.Objects;

public enum Gender implements BaseEnum {
    MALE("1", "male", "男"),
    FEMALE("2", "female", "女"),
    UNKNOWN("0","unknown", "未知");

    private String id;
    private String code;
    private String name;

    Gender(String id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public static Gender getById(final String id) {
        for(final Gender gender : values()) {
            if (Objects.equals(gender.id, id)) {
                return gender;
            }
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
