package com.haihua.hhplms.wf.service;

import com.haihua.hhplms.common.entity.BaseEnum;

public enum BizCode implements BaseEnum {
    COMPLETE_COMPANY_INFO_CREATION("ccic", "完成企业信息创建");

    private String code;
    private String name;
    BizCode(String code, String name) {
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
