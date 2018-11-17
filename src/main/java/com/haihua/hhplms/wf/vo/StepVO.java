package com.haihua.hhplms.wf.vo;


import com.haihua.hhplms.wf.entity.Route;
import com.haihua.hhplms.wf.entity.Step;

public class StepVO {
    private Long id;
    private String code;
    private String name;
    private String desc;
    private String assignedType;
    private String assignedTo;
    private String relatedView;

    public StepVO(Step step) {
        this.id = step.getSid();
        this.code = step.getCode();
        this.name = step.getName();
        this.desc = step.getDesc();
    }

    public StepVO(Step step, Route routeFragment) {
        this(step);
        this.assignedType = routeFragment.getAssignedType().getCode();
        this.assignedTo = routeFragment.getAssignedTo();
        this.relatedView = routeFragment.getRelatedView();
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

    public String getAssignedType() {
        return assignedType;
    }

    public void setAssignedType(String assignedType) {
        this.assignedType = assignedType;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getRelatedView() {
        return relatedView;
    }

    public void setRelatedView(String relatedView) {
        this.relatedView = relatedView;
    }
}
