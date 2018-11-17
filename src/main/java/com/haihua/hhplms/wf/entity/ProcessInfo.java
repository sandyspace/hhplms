package com.haihua.hhplms.wf.entity;

import com.haihua.hhplms.common.entity.BaseEntity;

import java.util.Objects;

public class ProcessInfo extends BaseEntity {
    private String code;
    private String name;
    private String desc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProcessInfo)) return false;
        if (!super.equals(o)) return false;
        ProcessInfo processInfo = (ProcessInfo) o;
        return Objects.equals(code, processInfo.code);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), code);
    }
}
