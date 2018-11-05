package com.haihua.hhplms.ana.vo;


import com.haihua.hhplms.ana.entity.Employee;

import java.util.List;

public class EmployeeVO {
    private Long id;
    private String loginName;
    private String realName;
    private String email;
    private String mobile;
    private String tel;
    private String gender;
    private String idCard;
    private String title;
    private String headImgUrl;
    private String status;
    private Integer versionNum;
    private List<RoleVO> grantedRoles;
    private List<PermissionVO> grantedPermissions;
    private List<PermissionVO> grantedApiList;

    public EmployeeVO(Employee employee) {
        this.id = employee.getSid();
        this.loginName = employee.getLoginName();
        this.realName = employee.getRealName();
        this.email = employee.getEmail();
        this.mobile = employee.getMobile();
        this.tel = employee.getTel();
        this.gender = employee.getGender().getCode();
        this.idCard = employee.getIdCard();
        this.title = employee.getTitle();
        this.headImgUrl = employee.getHeadImgUrl();
        this.status = employee.getStatus().getCode();
        this.versionNum = employee.getVersionNum();
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
