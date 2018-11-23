package com.haihua.hhplms.wf.service;

import com.haihua.hhplms.wf.entity.ProcessExecution;

public interface BizProcessHandler {
    void handleBizProcess(BizCode bizCode, ProcessExecution processExecution);
}
