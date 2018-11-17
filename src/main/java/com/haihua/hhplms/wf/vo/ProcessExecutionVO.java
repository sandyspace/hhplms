package com.haihua.hhplms.wf.vo;

import com.haihua.hhplms.common.utils.EnumUtil;
import com.haihua.hhplms.wf.entity.ProcessExecution;
import com.haihua.hhplms.wf.entity.ProcessInfo;
import com.haihua.hhplms.wf.entity.Step;

import java.util.Objects;

public class ProcessExecutionVO {
    private Long id;
    private ProcessInfoVO process;
    private String processStatus;
    private StepVO currentStep;
    private String stepStatus;
    private String activeFlag;
    private String checkedBy;
    private Long checkedTime;
    private String initBy;
    private Long initTime;

    public ProcessExecutionVO(ProcessExecution processExecution, ProcessInfo processInfo, Step step) {
        this.id = processExecution.getSid();
        this.process = new ProcessInfoVO(processInfo);
        this.processStatus = processExecution.getProcessStatus().getCode();
        this.currentStep = new StepVO(step);
        this.stepStatus = processExecution.getStepStatus().getCode();
        this.activeFlag = processExecution.getActiveFlag();
        this.checkedBy = processExecution.getCheckedBy();
        this.checkedTime = Objects.isNull(processExecution.getCheckedTime()) ? null : processExecution.getCheckedTime().getTime();
        this.initBy = processExecution.getInitBy();
        this.initTime = Objects.isNull(processExecution.getInitTime()) ? null : processExecution.getInitTime().getTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProcessInfoVO getProcess() {
        return process;
    }

    public void setProcess(ProcessInfoVO process) {
        this.process = process;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public StepVO getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(StepVO currentStep) {
        this.currentStep = currentStep;
    }

    public String getStepStatus() {
        return stepStatus;
    }

    public void setStepStatus(String stepStatus) {
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

    public Long getCheckedTime() {
        return checkedTime;
    }

    public void setCheckedTime(Long checkedTime) {
        this.checkedTime = checkedTime;
    }

    public String getInitBy() {
        return initBy;
    }

    public void setInitBy(String initBy) {
        this.initBy = initBy;
    }

    public Long getInitTime() {
        return initTime;
    }

    public void setInitTime(Long initTime) {
        this.initTime = initTime;
    }
}
