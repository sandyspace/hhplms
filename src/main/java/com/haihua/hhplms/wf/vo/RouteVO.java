package com.haihua.hhplms.wf.vo;

import com.haihua.hhplms.wf.entity.Route;

public class RouteVO {
    private Long id;
    private Long processId;
    private Long fromStepId;
    private Long toStepId;
    private String assignedType;
    private String assignedTo;
    private String startFlag;
    private String relatedView;
    private String viewOnChecking;
    private String attachedBiz;

    public RouteVO(Route route) {
        this.id = route.getSid();
        this.processId = route.getProcessSid();
        this.fromStepId = route.getFromStepSid();
        this.toStepId = route.getToStepSid();
        this.assignedType = route.getAssignedType().getCode();
        this.assignedTo = route.getAssignedTo();
        this.startFlag = route.getStartFlag();
        this.relatedView = route.getRelatedView();
        this.viewOnChecking = route.getViewOnChecking();
        this.attachedBiz = route.getAttachedBiz();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getFromStepId() {
        return fromStepId;
    }

    public void setFromStepId(Long fromStepId) {
        this.fromStepId = fromStepId;
    }

    public Long getToStepId() {
        return toStepId;
    }

    public void setToStepId(Long toStepId) {
        this.toStepId = toStepId;
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

    public String getViewOnChecking() {
        return viewOnChecking;
    }

    public void setViewOnChecking(String viewOnChecking) {
        this.viewOnChecking = viewOnChecking;
    }

    public String getAttachedBiz() {
        return attachedBiz;
    }

    public void setAttachedBiz(String attachedBiz) {
        this.attachedBiz = attachedBiz;
    }
}
