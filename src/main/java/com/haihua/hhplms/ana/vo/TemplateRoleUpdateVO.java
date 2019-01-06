package com.haihua.hhplms.ana.vo;

import com.haihua.hhplms.common.utils.JsonUtil;

public class TemplateRoleUpdateVO {
    private String name;
    private String memo;

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

    public String toString() {
        return JsonUtil.toJson(this);
    }
}
