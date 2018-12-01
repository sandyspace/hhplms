package com.haihua.hhplms.ana.entity;

import com.haihua.hhplms.common.entity.BaseEntity;
import com.haihua.hhplms.common.entity.BaseEnum;

import java.util.Objects;


public class CompanyInfo extends BaseEntity {
    private String code;
    private String name;
    private Type type;
    private String address;
    private String contactName;
    private String contactPhone;
    private Status status;

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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
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
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), code);
    }

    public enum Type implements BaseEnum {
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
        Type(String code, String name) {
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
