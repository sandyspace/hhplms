package com.haihua.hhplms.wf.entity;

import com.haihua.hhplms.common.entity.BaseEntity;
import com.haihua.hhplms.common.entity.BaseEnum;

import java.util.Objects;

public class Route extends BaseEntity {
    private Long processSid;
    private Long fromStepSid;
    private Long toStepSid;
    private AssignedType assignedType;
    private String assignedTo;
    private String startFlag;
    private String relatedView;

    public Long getProcessSid() {
        return processSid;
    }

    public void setProcessSid(Long processSid) {
        this.processSid = processSid;
    }

    public Long getFromStepSid() {
        return fromStepSid;
    }

    public void setFromStepSid(Long fromStepSid) {
        this.fromStepSid = fromStepSid;
    }

    public Long getToStepSid() {
        return toStepSid;
    }

    public void setToStepSid(Long toStepSid) {
        this.toStepSid = toStepSid;
    }

    public AssignedType getAssignedType() {
        return assignedType;
    }

    public void setAssignedType(AssignedType assignedType) {
        this.assignedType = assignedType;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getStartFlag() {
        return startFlag;
    }

    public void setStartFlag(String startFlag) {
        this.startFlag = startFlag;
    }

    public String getRelatedView() {
        return relatedView;
    }

    public void setRelatedView(String relatedView) {
        this.relatedView = relatedView;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Route)) return false;
        if (!super.equals(o)) return false;
        Route that = (Route) o;
        return Objects.equals(processSid, that.processSid) &&
                Objects.equals(fromStepSid, that.fromStepSid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), processSid, fromStepSid);
    }

    public enum AssignedType implements BaseEnum {
        ROLE("role", "角色"),
        INDIVIDUAL("individual", "个人");

        private String code;
        private String name;
        AssignedType(String code, String name) {
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
