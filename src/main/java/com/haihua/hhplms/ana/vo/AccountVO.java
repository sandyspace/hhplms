package com.haihua.hhplms.ana.vo;


import com.haihua.hhplms.ana.entity.Account;

import java.util.List;

public class AccountVO {
    private Long id;
    private String loginName;
    private String nickName;
    private String realName;
    private String mobile;
    private String email;
    private String gender;
    private String headImgUrl;
    private String type;
    private String status;
    private Long companyId;
    private Integer versionNum;
    private List<RoleVO> grantedRoles;
    private List<PermissionVO> grantedPermissions;
    private List<PermissionVO> grantedApiList;

    public AccountVO(Account account) {
        this.id = account.getSid();
        this.loginName = account.getLoginName();
        this.nickName = account.getNickName();
        this.realName = account.getRealName();
        this.mobile = account.getMobile();
        this.email = account.getEmail();
        this.gender = account.getGender().getCode();
        this.headImgUrl = account.getHeadImgUrl();
        this.type = account.getType().getCode();
        this.status = account.getStatus().getCode();
        this.companyId = account.getCompanyInfoSid();
        this.versionNum = account.getVersionNum();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Integer getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(Integer versionNum) {
        this.versionNum = versionNum;
    }

    public List<RoleVO> getGrantedRoles() {
        return grantedRoles;
    }

    public void setGrantedRoles(List<RoleVO> grantedRoles) {
        this.grantedRoles = grantedRoles;
    }

    public List<PermissionVO> getGrantedPermissions() {
        return grantedPermissions;
    }

    public void setGrantedPermissions(List<PermissionVO> grantedPermissions) {
        this.grantedPermissions = grantedPermissions;
    }

    public List<PermissionVO> getGrantedApiList() {
        return grantedApiList;
    }

    public void setGrantedApiList(List<PermissionVO> grantedApiList) {
        this.grantedApiList = grantedApiList;
    }
}
