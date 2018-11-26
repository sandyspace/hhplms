package com.haihua.hhplms.wf.service;

import com.haihua.hhplms.common.model.PageWrapper;
import com.haihua.hhplms.wf.entity.ProcessExecution;
import com.haihua.hhplms.wf.vo.ProcessExecutionVO;

import java.util.List;

public interface ProcessExecutionService {
    PageWrapper<List<ProcessExecutionVO>> loadProcessExecutionsByPage(Long processSid,
                                                                      String processStatus,
                                                                      String currentStepSid,
                                                                      String stepStatus,
                                                                      String activeFlag,
                                                                      Long checkedTimeFrom,
                                                                      Long checkedTimeTo,
                                                                      Long initTimeFrom,
                                                                      Long initTimeTo,
                                                                      Integer pageNo,
                                                                      Integer pageSize);
    List<ProcessExecutionVO> getProcessExecutionsLaunchedByAccount(Long processSid);
    void checkProcessExecution(Long processExecutionSid);
    ProcessExecution initProcess(String processCode, Long ownerSid);
}
