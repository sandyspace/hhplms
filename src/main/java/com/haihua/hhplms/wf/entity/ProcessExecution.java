package com.haihua.hhplms.wf.entity;

import com.haihua.hhplms.ana.entity.Role;
import com.haihua.hhplms.common.entity.BaseEntity;
import com.haihua.hhplms.common.entity.BaseEnum;

import java.util.Date;
import java.util.Objects;

public class ProcessExecution extends BaseEntity {
    private Long processSid;
    private Role.Category processOwner;
    private Long ownerSid;
    private String processInstanceId;
    private ProcessStatus processStatus;
    private Long currentStepSid;
    private Route.AssignedType assignedType;
    private String assignedTo;
    private StepStatus stepStatus;
    private String activeFlag;
    private String checkedBy;
    private Date checkedTime;
    private String initBy;
    private Date initTime;

    public Long getProcessSid() {
        return processSid;
    }

    public void setProcessSid(Long processSid) {
        this.processSid = processSid;
    }

    public Role.Category getProcessOwner() {
        return processOwner;
    }

    public void setProcessOwner(Role.Category processOwner) {
        this.processOwner = processOwner;
    }

    public Long getOwnerSid() {
        return ownerSid;
    }

    public void setOwnerSid(Long ownerSid) {
        this.ownerSid = ownerSid;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public ProcessStatus getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(ProcessStatus processStatus) {
        this.processStatus = processStatus;
    }

    public Long getCurrentStepSid() {
        return currentStepSid;
    }

    public void setCurrentStepSid(Long currentStepSid) {
        this.currentStepSid = currentStepSid;
    }

    public Route.AssignedType getAssignedType() {
        return assignedType;
    }

    public void setAssignedType(Route.AssignedType assignedType) {
        this.assignedType = assignedType;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public StepStatus getStepStatus() {
        return stepStatus;
    }

    public void setStepStatus(StepStatus stepStatus) {
        this.stepStatus = stepStatus;
    }

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }

    public String getCheckedBy() {
        return checkedBy;
    }

    public void setCheckedBy(String checkedBy) {
        this.checkedBy = checkedBy;
    }

    public Date getCheckedTime() {
        return checkedTime;
    }

    public void setCheckedTime(Date checkedTime) {
        this.checkedTime = checkedTime;
    }

    public String getInitBy() {
        return initBy;
    }

    public void setInitBy(String initBy) {
        this.initBy = initBy;
    }

    public Date getInitTime() {
        return initTime;
    }

    public void setInitTime(Date initTime) {
        this.initTime = initTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProcessExecution)) return false;
        if (!super.equals(o)) return false;
        ProcessExecution that = (ProcessExecution) o;
        return Objects.equals(processSid, that.processSid) &&
                Objects.equals(processInstanceId, that.processInstanceId) &&
                Objects.equals(currentStepSid, that.currentStepSid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), processSid, processInstanceId, currentStepSid);
    }

    public enum StepStatus implements BaseEnum {
        WAIT("wait", "等待"),
        PROCESS("process", "处理中"),
        FINISH("finish", "完成"),
        ERROR("error", "失败");

        private String code;
        private String name;
        StepStatus(String code, String name) {
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

    public enum ProcessStatus implements BaseEnum {
        PROCESSING("processing", "处理中"),
        COMPLETED("completed", "已完成"),
        REJECTED("rejected", "已拒绝"),
        FAILED("failed", "已失败");

        private String code;
        private String name;
        ProcessStatus(String code, String name) {
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
