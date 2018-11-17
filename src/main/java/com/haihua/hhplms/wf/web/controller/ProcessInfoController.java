package com.haihua.hhplms.wf.web.controller;

import com.haihua.hhplms.common.model.ResultBean;
import com.haihua.hhplms.wf.service.ProcessInfoService;
import com.haihua.hhplms.wf.vo.ProcessInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProcessInfoController {
    @Autowired
    @Qualifier("processInfoService")
    private ProcessInfoService processInfoService;

    @GetMapping(path = "/api/wf/processes/available", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<ProcessInfoVO>> loadAvailableProcesses() {
        return ResultBean.Success.of(processInfoService.getAvailableProcesses(), "");
    }

    @GetMapping(path = "/api/wf/processes/{code}/detail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<ProcessInfoVO> loadDetail(@PathVariable("code") String code) {
        return ResultBean.Success.of(processInfoService.loadDetail(code), "");
    }
}
