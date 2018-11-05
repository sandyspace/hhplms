package com.haihua.hhplms.ana.entity;

import com.haihua.hhplms.common.entity.BaseEntity;
import com.haihua.hhplms.common.entity.BaseEnum;

import java.util.Objects;


public class CompanyInfo extends BaseEntity {
    private String companyCode;
    private String companyName;
    private CompanyType companyType;
    private String contactName;
    private String contactMobile;
    private Status status;

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public CompanyType getCompanyType() {
        return companyType;
    }

    public void setCompanyType(CompanyType companyType) {
        this.companyType = companyType;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompanyInfo)) return false;
        if (!super.equals(o)) return false;
        CompanyInfo that = (CompanyInfo) o;
        return Objects.equals(companyCode, that.companyCode);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), companyCode);
    }

    public enum CompanyType implements BaseEnum {
        /*llc(Limited Liability Company): 有限责任公司,
          jsc(Joint Stock (limited) Company): 股份有限公司,
          soe(state-owned enterprises): 国有企业,
          pb(private business): 私营企业,
          lp(limited partnership): 有限合伙企业,
          fie(foreign invested enterprise): 外商投资企业,
          sp(sole proprietorship): 个人独资企业,
          hmt(HongKong Macau Taiwan)港澳台,
          other: 其他
         */
        LIMITED_LIABILITY_COMPANY("llc", "有限责任公司"),
        JOINT_STOCK_COMPANY("jsc", "股份有限公司"),
        STATE_OWNED_ENTERPRISE("soe", "国有企业"),
        PRIVATE_BUSINESS("pb", "私营企业"),
        LIMITED_PARTNERSHIP("lp", "有限合伙企业"),
        FOREIGN_INVTESTED_ENTERPRISE("fie", "外商投资企业"),
        SOLE_PROPRIETORSHIP("sp", "个人独资企业"),
        HONGKONG_MACAU_TAIWAN("hmt", "港澳台"),
        OTHER("other", "其他");
        private String code;
        private String name;
        CompanyType(String code, String name) {
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

    public enum Status implements BaseEnum {
        VERIFYING("verifying", "审核中"),
        APPROVED("approved", "已通过");
        private String code;
        private String name;
        Status(String code, String name) {
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
}
