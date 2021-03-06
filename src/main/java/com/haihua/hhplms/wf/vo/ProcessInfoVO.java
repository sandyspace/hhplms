package com.haihua.hhplms.wf.vo;

import com.haihua.hhplms.wf.entity.ProcessInfo;

import java.util.List;

public class ProcessInfoVO {
    private Long id;
    private String code;
    private String name;
    private String desc;
    private List<StepVO> steps;
    private List<ProcessExecutionVO> processExecutions;

    public ProcessInfoVO(Long id) {
        this.id = id;
    }

    public ProcessInfoVO(ProcessInfo processInfo) {
        this(processInfo.getSid());
        this.code = processInfo.getCode();
        this.name = processInfo.getName();
        this.desc = processInfo.getDesc();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<StepVO> getSteps() {
        return steps;
    }

    public void setSteps(List<StepVO> steps) {
        this.steps = steps;
    }

    public List<ProcessExecutionVO> getProcessExecutions() {
        return processExecutions;
    }

    public void setProcessExecutions(List<ProcessExecutionVO> processExecutions) {
        this.processExecutions = processExecutions;
    }
}
