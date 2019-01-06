package com.haihua.hhplms.ana.vo;

import com.haihua.hhplms.common.utils.JsonUtil;

public class TemplateRoleCreationVO {
    private String code;
    private String name;
    private String memo;
    private String type;

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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        return JsonUtil.toJson(this);
    }
}
