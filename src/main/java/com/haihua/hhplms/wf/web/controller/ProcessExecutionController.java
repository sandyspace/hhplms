package com.haihua.hhplms.wf.web.controller;

import com.haihua.hhplms.common.model.PageWrapper;
import com.haihua.hhplms.common.model.ResultBean;
import com.haihua.hhplms.wf.entity.ProcessExecution;
import com.haihua.hhplms.wf.service.ProcessExecutionService;
import com.haihua.hhplms.wf.vo.ProcessExecutionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProcessExecutionController {
    @Autowired
    @Qualifier("processExecutionService")
    private ProcessExecutionService processExecutionService;

    @GetMapping(path = "/api/wf/processExecutions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<PageWrapper<List<ProcessExecutionVO>>> loadProcessExecutions(@RequestParam(name = "processId", required = false) Long processSid,
                                                                                           @RequestParam(name = "processStatus", required = false) String processStatus,
                                                                                           @RequestParam(name = "currentStepId", required = false) String currentStepSid,
                                                                                           @RequestParam(name = "stepStatus", required = false) String stepStatus,
                                                                                           @RequestParam(name = "activeFlag", required = false) String activeFlag,
                                                                                           @RequestParam(name = "checkedTimeFrom", required = false) Long checkedTimeFrom,
                                                                                           @RequestParam(name = "checkedTimeTo", required = false) Long checkedTimeTo,
                                                                                           @RequestParam(name = "initTimeFrom", required = false) Long initTimeFrom,
                                                                                           @RequestParam(name = "initTimeTo", required = false) Long initTimeTo,
                                                                                           @RequestParam(name = "pageNo") Integer pageNo,
                                                                                           @RequestParam(name = "pageSize") Integer pageSize) {
        PageWrapper<List<ProcessExecutionVO>> pageOfProcessExecutions = processExecutionService.loadProcessExecutionsByPage(processSid,
                processStatus, currentStepSid, stepStatus, activeFlag,
                checkedTimeFrom, checkedTimeTo, initTimeFrom, initTimeTo,
                pageNo, pageSize);
        return ResultBean.Success.of(pageOfProcessExecutions, "");
    }

    @PostMapping(path = "/api/wf/processExecutions/{id}/check", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> checkProcessExecution(@PathVariable("id") Long processExecutionSid) {
        processExecutionService.checkProcessExecution(processExecutionSid);
        return ResultBean.Success.of(processExecutionSid, "");
    }
}
