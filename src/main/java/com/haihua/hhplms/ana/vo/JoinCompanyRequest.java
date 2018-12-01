package com.haihua.hhplms.ana.vo;

public class JoinCompanyRequest {
    private Long companyId;
    private String joinPersonName;
    private String joinPersonSex;

    public JoinCompanyRequest() {

    }

    public JoinCompanyRequest(Long companyId, String joinPersonName, String joinPersonSex) {
        this.companyId = companyId;
        this.joinPersonName = joinPersonName;
        this.joinPersonSex = joinPersonSex;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getJoinPersonName() {
        return joinPersonName;
    }

    public void setJoinPersonName(String joinPersonName) {
        this.joinPersonName = joinPersonName;
    }

    public String getJoinPersonSex() {
        return joinPersonSex;
    }

    public void setJoinPersonSex(String joinPersonSex) {
        this.joinPersonSex = joinPersonSex;
    }
}
