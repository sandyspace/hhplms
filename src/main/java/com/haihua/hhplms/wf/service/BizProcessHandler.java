package com.haihua.hhplms.wf.service;

import com.haihua.hhplms.wf.entity.ProcessExecution;
import com.haihua.hhplms.wf.entity.Route;

public interface BizProcessHandler {
    void handleBizProcess(Route.BizCode bizCode, ProcessExecution processExecution);
}
